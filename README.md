# 自定义控件

## 代码托管
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.wandersnail/widget-generic/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.wandersnail/widget-generic)
[![Download](https://api.bintray.com/packages/wandersnail/androidx/widget-generic/images/download.svg) ](https://bintray.com/wandersnail/androidx/widget-generic/_latestVersion)

## 使用

1. module的build.gradle中的添加依赖，自行修改为最新版本，同步后通常就可以用了：
```
dependencies {
	...
	implementation 'cn.wandersnail:widget-generic:latestVersion'
	implementation 'androidx.recyclerview:recyclerview:latestVersion'
}
```

2. 如果从jcenter下载失败。在project的build.gradle里的repositories添加内容，最好两个都加上，添加完再次同步即可。
```
allprojects {
	repositories {
		...
		mavenCentral()
		maven { url 'https://dl.bintray.com/wandersnail/androidx/' }
	}
}
```

## 功能

- 自定义对话框基类
- 圆形ImageView
- 圆形进度条
- 带清除EditText
- 任意圆角Button和TextView
- 带动画开关
- 滑动文本选择器

![image](https://gitee.com/fszeng/widget-generic/raw/master/images/device-2019-04-06-152951.png)
![image](https://gitee.com/fszeng/widget-generic/raw/master/images/device-2019-04-06-152423.gif)
![image](https://images.gitee.com/uploads/images/2020/0229/120750_216d1e79_543181.gif)
![image](https://gitee.com/fszeng/widget-generic/raw/master/images/device-2019-04-06-152652.gif)
![image](https://images.gitee.com/uploads/images/2020/0229/120748_340b52df_543181.gif)
![image](https://images.gitee.com/uploads/images/2020/0229/120752_8ad136e6_543181.gif)
![image](https://images.gitee.com/uploads/images/2020/0229/120753_084111be_543181.gif)
![image](https://gitee.com/fszeng/widget-generic/raw/master/images/device-2019-04-06-224935.gif)