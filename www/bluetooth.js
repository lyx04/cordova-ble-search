var exec = require('cordova/exec');

var arg0 = 'bluetooth_message';


exports.coolMethod = function (success, error) {
    exec(success, error, 'bluetooth', 'coolMethod', [arg0]);
};
exports.registerReceiver = function(success,error){   //注册监听的js方法
    exec(success,error,"bluetooth","registerReceiver",[arg0]);
}
exports.unregisterReceiver = function(success,error){
    exec(success,error,"bluetooth","unregisterReceiver",[arg0]);  //取消监听的js方法
}
