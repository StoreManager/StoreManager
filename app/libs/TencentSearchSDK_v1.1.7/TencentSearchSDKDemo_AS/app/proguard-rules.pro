# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/wangxiaokun/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#腾讯地图 2D sdk
-libraryjars libs/TencentMapSDK_Raster_v1.2.3.20160630.jar
-keep class com.tencent.mapsdk.**{*;}
-keep class com.tencent.tencentmap.**{*;}

#腾讯地图检索sdk
-libraryjars libs/TencentSearch1.1.3.jar
-keep class com.tencent.lbssearch.**{*;}
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-dontwarn com.tencent.lbssearch.**
