# 自定义控件

## 代码托管
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.wandersnail/widget-generic/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.wandersnail/widget-generic)
[![Release](https://jitpack.io/v/cn.wandersnail/widget-generic.svg)](https://jitpack.io/#cn.wandersnail/widget-generic)
[![](https://img.shields.io/badge/源码-github-blue.svg)](https://github.com/wandersnail/widget-generic)
[![](https://img.shields.io/badge/源码-码云-blue.svg)](https://gitee.com/fszeng/widget-generic)
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
		maven { url 'https://jitpack.io' }
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

![image](https://s2.ax1x.com/2020/02/29/3shmWD.png)
![image](https://s2.ax1x.com/2020/02/29/3shalQ.gif)
![image](https://s2.ax1x.com/2020/02/29/3shKQH.gif)
![image](https://s2.ax1x.com/2020/02/29/3shYY8.gif)
![image](https://s2.ax1x.com/2020/02/29/3shQOA.gif)
![image](https://s2.ax1x.com/2020/02/29/3sh3wt.gif)
![image](https://s2.ax1x.com/2020/02/29/3shdyj.gif)
![image](https://s2.ax1x.com/2020/02/29/3sIMy4.gif)