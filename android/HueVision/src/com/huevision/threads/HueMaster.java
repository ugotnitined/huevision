package com.huevision.threads;

import android.util.Log;


public class HueMaster extends Thread{
	protected int ref;
	protected HueWriter writer;
	protected SensorListener listener;
	static final double P = .2;
	
	public HueMaster(HueWriter writer, SensorListener listener){
		this.ref = 1759;
		this.writer = writer;
		this.listener = listener;
	}
	
	public void nullifyRef(){
		this.ref = 1759;
	}
	
	public void setRef(){
		this.ref = (int)Math.round(listener.getValue());
	}
	
	public void run(){
		while(true){
			if (this.ref!=1759){
				double err = ref-listener.getValue();
				int newVal = (int)(writer.getBri() + Math.floor(P*err));
				writer.setBri(newVal>255?255:(newVal<0?0:newVal));
				//Log.d("FLAPPY","deriv:"+Math.floor(P*err)+" sensor: "+listener.getValue()+" ref: "+ref);
			}
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){
				// shut up
			}
		}
	}
}
