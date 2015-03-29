/*
	This file is part of the Android application TarotDroid.
 	
	TarotDroid is free software: you can redistribute it and/or modify
 	it under the terms of the GNU General Public License as published by
 	the Free Software Foundation, either version 3 of the License, or
 	(at your option) any later version.
 	
 	TarotDroid is distributed in the hope that it will be useful,
 	but WITHOUT ANY WARRANTY; without even the implied warranty of
 	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 	GNU General Public License for more details.
 	
 	You should have received a copy of the GNU General Public License
 	along with TarotDroid. If not, see <http://www.gnu.org/licenses/>.
*/
package org.nla.tarotdroid.lib.helpers;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nla.tarotdroid.lib.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
 *
 */
public final class BluetoothHelper {

	/**
	 * Constant to use in activity's setActivityResult().
	 */
	public static final int REQUEST_SET_DISCOVERABLE = 1;
	
	/**
	 * The current activity.
	 */
	private Activity activity;
	
	/**
	 * The bluetooth adapter.
	 */
	private BluetoothAdapter bluetoothAdapter;
	
	/**
	 * The broadcast receiver.
	 */
	private BroadcastReceiver broadcastReceiver;
	
	/**
	 * The discovered devices.
	 */
	private List<BluetoothDevice> discoveredDevices;
	
	/**
	 * The discovered devices as a Map.
	 */
	private Map<String, BluetoothDevice> mapNamesDevices;
	
	/**
	 * The progress dialog.
	 */
	private ProgressDialog dialog;
	
	/**
	 * Constructor.
	 * @param broadcastReceiver
	 */
	public BluetoothHelper(final Context context) {
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		}
		
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.discoveredDevices = new ArrayList<BluetoothDevice>();
		this.mapNamesDevices = new HashMap<String, BluetoothDevice>();
	}
	
	/**
	 * Sets the activity.
	 * @param activity the activity.
	 */
	public void setActivity(final Activity activity) {
		if (activity == null) {
			throw new IllegalArgumentException("activity is null");
		}
		this.activity = activity;
		this.dialog = new ProgressDialog(this.activity);
		this.broadcastReceiver = new CustomBroadcastRecever();
	}
	
	/**
	 * Returns the singleton.
	 * @return the singleton.
	 */
	public BluetoothAdapter getBluetoothAdapter() {
		return this.bluetoothAdapter;
	}
	
	/**
	 * Indicates whether bluetooth is supported by device.
	 * @return
	 */
	public boolean isBluetoothSupported() {
		return this.bluetoothAdapter != null;
	}
	
	/**
	 * Returns true if bluetooth is enabled.
	 * @return true if bluetooth is enabled, false otherwise.
	 */
	public boolean isBluetoothEnabled() {
		return this.bluetoothAdapter.isEnabled();
	}
	
//	/**
//	 * Turns on bluetooth on the device.
//	 */
//	public void enableBluetooth() {
//		if (!this.isBluetoothEnabled()) {
//		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//		    this.activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//		}		
//	}
	
	/**
	 * Starts discovering devices.
	 */
	public void startDiscovery() {
		checkArgument(this.activity != null, "you must first set an activity...");
		this.discoveredDevices.clear();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		this.activity.registerReceiver(this.broadcastReceiver, filter);
		this.bluetoothAdapter.startDiscovery();
	}
	
	/**
	 * Cancels the devices discovery.
	 */
	public void cancelDiscovery() {
		checkArgument(this.activity != null, "you must first set an activity...");
		this.bluetoothAdapter.cancelDiscovery();
		this.activity.unregisterReceiver(this.broadcastReceiver);
	}

	/**
	 * Sets the device discoverable.
	 */
	public void setBluetoothDeviceDiscoverable() {
		checkArgument(this.activity != null, "you must first set an activity...");
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
		this.activity.startActivityForResult(discoverableIntent, BluetoothHelper.REQUEST_SET_DISCOVERABLE);
	}
	
	/**
	 * @return
	 */
	public int getBluetoothDeviceCount() {
		return this.discoveredDevices.size();
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getBluetoothDeviceNames() {
		String[] names = new String[this.mapNamesDevices.size()];
		int i = 0;
		for (String name : this.mapNamesDevices.keySet()) {
			names[i] = name;
			++i;
		}
		return names;
	}
	
	/**
	 * @return
	 */
	public BluetoothDevice getFirstBluetoothDevice() {
		if (this.discoveredDevices.size() == 0) {
			return null;
		}
		return this.discoveredDevices.get(0);
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public BluetoothDevice getBluetoothDevice(final String name) {
		if (name == null) {
			return null;
		}
		return this.mapNamesDevices.get(name);
	}
	
	/**
	 * @author Nicolas LAURENT daffycricket<a>yahoo.fr
	 *
	 */
	private class CustomBroadcastRecever extends BroadcastReceiver {
		
		/* (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
		 */
		@Override
		public void onReceive(final Context context, final Intent intent) {
	        String action = intent.getAction();
	        
	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	        	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        	BluetoothHelper.this.discoveredDevices.add(device);
	        	BluetoothHelper.this.mapNamesDevices.put(device.getName(), device);
	        	
	        	int msgRunnningScanId = BluetoothHelper.this.discoveredDevices.size() == 1 ? R.string.msgRunnningScanSolo : R.string.msgRunnningScanMulti;
	        	
	        	String bluetoothDevices = "";
	        	for (BluetoothDevice bluetoothDevice : BluetoothHelper.this.discoveredDevices) {
	        		bluetoothDevices += bluetoothDevice.getName() + "|";
	        	}
	        	
	        	
	        	//BluetoothHelper.this.dialog.setMessage(context.getResources().getString(msgRunnningScanId, BluetoothHelper.this.discoveredDevices.size()));
	        	BluetoothHelper.this.dialog.setMessage(context.getResources().getString(msgRunnningScanId, bluetoothDevices));
	        }
	        
	        // when discovery finishes
	        else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
	    		// hide busy idicator
	    		if (BluetoothHelper.this.dialog.isShowing()) {
	    			BluetoothHelper.this.dialog.dismiss();
	    		}
				// stop discovering devices
				BluetoothHelper.this.cancelDiscovery();
	        }
	        
	        // when discovery starts
	        else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {				
	        	BluetoothHelper.this.discoveredDevices.clear();
	        	BluetoothHelper.this.mapNamesDevices.clear();
	        	BluetoothHelper.this.dialog.setMessage(context.getResources().getString(R.string.msgRunnningScanSolo, BluetoothHelper.this.discoveredDevices.size()));
	        	BluetoothHelper.this.dialog.show();
	        }
		}
	}
}
