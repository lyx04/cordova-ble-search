package bluetooth;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;
/**
 * This class echoes a string called from JavaScript.
 */
public class bluetooth extends CordovaPlugin {
    BroadcastReceiver mReceiver =null;
    IntentFilter filterFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver(mReceiver, filterFound);

    IntentFilter filterStart = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
    registerReceiver(mReceiver, filterStart);

    IntentFilter filterFinish = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    registerReceiver(mReceiver, filterFinish);
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }else if(action.equals("registerReceiver")){  //如果action==“registerReceiver”注册
            String message = args.getString(0);
            this.registerReceiver(callbackContext);   //自定义方法后面讲
            return true;
        }else if(action.equals("unregisterReceiver")){ 
            // 如果action==“unregisterReceiver” 取消
            this.unregisterReceiver(callbackContext);  //自定义方法后面讲
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
    private void registerReceiver(final CallbackContext callbackContext) throws JSONException {
        final JSONArray unpairedDevices = new JSONArray(); //new  JSONArray对象
        mReceiver = new BroadcastReceiver() {           //new广播对象
        @Override
        public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
        // Toast.makeText(context,"BroadcastReceiver",Toast.LENGTH_SHORT).show();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
				Log.d(TAG, "开始扫描...");
			}

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device 	!= null) {
					// 添加到ListView的Adapter。
                try {
                        JSONObject o = deviceToJSON(device,"CONNECTED");  //生成json格式的device信息
                        unpairedDevices.put(o);
                        if (callbackContext != null) {
                            PluginResult res = new PluginResult(PluginResult.Status.OK, o);//将信息写入 同时设置后续还有返回信息
                            res.setKeepCallback(true);
                            callbackContext.sendPluginResult(res); 
                        }
                    } catch (JSONException e) {}
				}
			}

			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
				Log.d(TAG, "扫描结束.");
			}
        };
        Activity activity = cordova.getActivity();
        activity.registerReceiver(mReceiver, makeFilter());
    }
     public void unregisterReceiver(final CallbackContext callbackContext){
        Activity activity = cordova.getActivity();
        activity.unregisterReceiver(mReceiver);
    }
    /*
    @deviceToJSON 将收到的设备对象转化为JSONObject对象方便与js交互数据
    @device 设备对象，当监听到设备变化后接受到的设备对象
    @connectType如果是连接发出的消息值为 CONNECTED 如果是断开连接发出的消息为 DISCONNECTED
    */
    private final JSONObject deviceToJSON(BluetoothDevice device,String connectType) throws JSONException {
        JSONObject json = new JSONObject();    //创建JSONObject对象
        json.put("name", device.getName());     //设备名字
        json.put("address", device.getAddress());   //设备地址
        json.put("id", device.getAddress());   //设备唯一编号使用地址表示
        json.put("connectType",connectType);
        if (device.getBluetoothClass() != null) {
            json.put("class", device.getBluetoothClass().getDeviceClass());  //设备类型 主要分别设备是哪一种设备
        }
        return json;
    }
    private IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        return filter;
    }

}
