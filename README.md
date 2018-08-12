# cordova-ble-search
关于一台设备有多个蓝牙地址导致cordova蓝牙插件不能如期搜索到想要的蓝牙设备地址
---首先有ionic2以及cordova环境，如果没有的话，请自行阅读官网教程
---鸣谢
---简书中的好书生:https://www.jianshu.com/u/d4b7d02dcf33
---与他的https://www.jianshu.com/p/ca7965c315b4

---在plugin.xml中AndroidManifest.xml里需要添加如下权限
```
<uses-permission android:name="android.permission.BLUETOOTH"/>
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```
