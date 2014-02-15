package com.huevision.lighting.quickstart;

import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;
import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import com.philips.lighting.quickstart.R;
//import com.philips.lighting.hue.sdk.utilities.impl.Color;
//import android.R;

/**
 * MyApplicationActivity - The starting point for creating your own Hue App.  
 * Currently contains a simple view with a button to change your lights to random colours.  Remove this and add your own app implementation here! Have fun!
 * 
 * @author SteveyO
 *
 */
public class ColorSelectorFragment extends Fragment implements OnColorChangedListener {
    private PHHueSDK phHueSDK;
    private static final int MAX_HUE=65535;
    public static final String TAG = "QuickStart";
    public BroadcastReceiver mReceiver;
    public BluetoothAdapter mBluetoothAdapter;
    
    private ColorPicker picker;
	private SaturationBar saturationBar;
	private ValueBar valueBar;
	private Button button;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_selector, container, false);
        
        phHueSDK = PHHueSDK.create();
        
        //setUpBluetooth();
        
        picker = (ColorPicker) rootView.findViewById(R.id.picker);
        saturationBar = (SaturationBar) rootView.findViewById(R.id.saturationbar);
        valueBar = (ValueBar) rootView.findViewById(R.id.valuebar);
        button = (Button) rootView.findViewById(R.id.buttonSet);

    
        //picker.addSaturationBar(saturationBar);
        //picker.addValueBar(valueBar);
        picker.setOnColorChangedListener(this);
        

        /**randomButton = (Button) findViewById(R.id.buttonRand);**/
        button.setOnClickListener(new OnClickListener() {

        	@Override
			public void onClick(View v) {
				picker.setOldCenterColor(picker.getColor());
				PHBridge bridge = phHueSDK.getSelectedBridge();
		        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
		        
		       
		        int color = picker.getColor();
		        float[] hsv = new float[3];
		        Color.colorToHSV(color, hsv);
		        
		        int hue = (int)(hsv[0]*182);
		        Log.d("hue", String.valueOf(hue));
		        for (PHLight light : allLights) {
		            PHLightState lightState = new PHLightState();
		            // To validate your lightstate is valid (before sending to the bridge) you can use:  
		            // String validState = lightState.validateState();
		            
		            lightState.setHue(hue);
		            bridge.updateLightState(light, lightState, listener);
		            //bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.
		        }		
        	}
        });
        return rootView;
    }
	

    
    @Override
	public void onColorChanged(int color) {
    	
	}

    /**
    public void setUpBluetooth(){
    	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //startActivityForResult(enableBtIntent, 1);
            Intent discoverableIntent = new
            Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 1000);
            startActivity(discoverableIntent);
        }
        
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    //Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d("bluetooth", "Device found");
                    Log.d("bluetooth", device.getName());
                    Log.d("bluetooth", device.getAddress());
                    // Add t\he name and address to an array adapter to show in a ListView
                    //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.getActivity().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        Log.d("bluetooth", "Receiver regitered");
        
        boolean success = mBluetoothAdapter.startDiscovery();
        Log.d("bluetooth", String.valueOf(success) );
        
        TelephonyManager tManager = (TelephonyManager) this.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
    	String uuid = tManager.getDeviceId();
    	
    } **/
    public void randomLights() {
    	Log.d("button", "button pressed");
    	//if(mBluetoothAdapter.isDiscovering()){
    	//	Log.d("bluetooth", "discovery restarted");
    	//	mBluetoothAdapter.cancelDiscovery();
    	//}
    	//mBluetoothAdapter.startDiscovery();
        PHBridge bridge = phHueSDK.getSelectedBridge();

        List<PHLight> allLights = bridge.getResourceCache().getAllLights();
        Random rand = new Random();
        
        for (PHLight light : allLights) {
            PHLightState lightState = new PHLightState();
            lightState.setHue(rand.nextInt(MAX_HUE));
            // To validate your lightstate is valid (before sending to the bridge) you can use:  
            // String validState = lightState.validateState();
            bridge.updateLightState(light, lightState, listener);
            //  bridge.updateLightState(light, lightState);   // If no bridge response is required then use this simpler form.
        }
    }
    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {
        
        @Override
        public void onSuccess() {  
        }
        
        @Override
        public void onStateUpdate(Hashtable<String, String> arg0, List<PHHueError> arg1) {
           Log.w(TAG, "Light has updated");
        }
        
        @Override
        public void onError(int arg0, String arg1) {  
        }
    };
    
    @Override
	public void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {
            
            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }
            
            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
        //this.getActivity().unregisterReceiver(mReceiver);
    }
}
