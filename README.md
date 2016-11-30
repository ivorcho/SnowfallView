# SnowfallView
[![](https://www.jitpack.io/v/ivorcho/SnowfallView.svg)](https://www.jitpack.io/#ivorcho/SnowfallView)

This is a small library which provides a simple View that animates snowfall using any image you provide to it as a snowflake. 

![SnowfallView](https://github.com/ivorcho/SnowfallView/blob/master/snowfall.gif)

# Importing the Library
Simply add the following in your project `build.gradle` file:
```groovy
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
}
```
Add the dependency to your module `build.gradle`:
```groovy
dependencies {
	   compile 'com.github.ivorcho:SnowfallView:1.0'
}
```

# Usage
Simply add the view to your layout. Make sure it is on top of all of your views so it is visible:
```xml
<com.ivorcho.snowfallview.SnowfallView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:snowflake="@drawable/ic_snowflake"
            app:spawnRate="400"/>
```

### Attributes
You can use the following attributes for customization:
* `snowflake` - Reference to a drawable which to be used as a snowflake. Coresponding method: `setFlakeImage()`
* `spawnRate` - Rate in milliseconds at which the snowflakes are spawned. Coresponding method: `setSnowflakeSpawnRate()`
