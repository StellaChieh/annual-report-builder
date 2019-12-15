# 建置

簽出專案:

~~~
git clone git@bitbucket.org:stella_chieh/cmt-report.git
~~~

使用 Gradle 進行建置:

~~~
cd annual-report-builder
gradle shadowJar
~~~

產出的 Jar 檔將會位於 `build/libs/` 資料夾下。

# 外部相依套件來源

* ChartDirector (授權檔案)


打包jar環境
~~~
projectRoot/config.properties
cmt.root.path 設為 空字串
surface.output.folder 設為 空字串
upperair.output.folder 設為 空字串
summary.output.folder 設為 空字串

projectRoot/config/config_interface/defaultFilePath.properties 全部清空

projectRoot/config/config_summmary/summary-spring-application.xml
<context:property-placeholder location="config.properties"/>

projectRoot/config/config_upperair/upperair-spring-application.xml
<context:property-placeholder location="config.properties"/>

projectRoot/config/config_surface/surface-spring-application.xml
<context:property-placeholder location="config.properties"/>

projectRoot/windRose/src/main/resources/myBatis-config.xml
<properties url="file:./config.properties"/>

app_interface的cwb.cmt.view.application.Main.java
runFromBat=true
~~~


IDE 開發
~~~
projectRoot/config.properties
cmt.root.path 設為 projectRoot絕對路徑

projectRoot/config/config_summmary/summary-spring-application.xml
<context:property-placeholder location="config.properties的絕對路徑"/>

projectRoot/config/config_upperair/upperair-spring-application.xml
<context:property-placeholder location="config.properties的絕對路徑"/>

projectRoot/config/config_surface/surface-spring-application.xml
<context:property-placeholder location="config.properties的絕對路徑"/>

projectRoot/windRose/src/main/resources/myBatis-config.xml
<properties url="file:./../config.properties"/>

app_interface的cwb.cmt.view.application.Main.java
runFromBat=false
~~~

