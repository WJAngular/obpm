echo ; > .\resource\common\obpm.main.all.js

echo ; >> .\resource\common\obpm.main.all.js
type .\resource\js\ratchet.min.js           		>> .\resource\common\obpm.main.all.js
echo ; >> .\resource\common\obpm.main.all.js
type .\resource\js\common.js              			>> .\resource\common\obpm.main.all.js
echo ; >> .\resource\common\obpm.main.all.js
type .\resource\js\homepage.js              		>> .\resource\common\obpm.main.all.js
echo ; >> .\resource\common\obpm.main.all.js
type .\resource\js\menu.js              		>> .\resource\common\obpm.main.all.js
echo ; >> .\resource\common\obpm.main.all.js
type .\resource\js\flowCenter.js           		>> .\resource\common\obpm.main.all.js
echo ; >> .\resource\common\obpm.main.all.js
type .\resource\js\jquery.swiper-3.3.1.min.js       >> .\resource\common\obpm.main.all.js
echo ; >> .\resource\common\obpm.main.all.js


java -jar yuicompressor-2.4.8.jar .\resource\common\obpm.main.all.js -o .\resource\common\obpm.main.all.min.js

type .\resource\css\ratchet.min.css         > .\resource\common\obpm.main.all.css   
type .\resource\css\weui.min.css 			>> .\resource\common\obpm.main.all.css   
type .\resource\css\swiper.min.css			>> .\resource\common\obpm.main.all.css 
type .\resource\css\font-awesome.min.css	>> .\resource\common\obpm.main.all.css   
type .\resource\css\global.css              >> .\resource\common\obpm.main.all.css   
type .\resource\css\animate.css             >> .\resource\common\obpm.main.all.css   
type .\resource\css\css.css                 >> .\resource\common\obpm.main.all.css   
type .\resource\css\launch.css              >> .\resource\common\obpm.main.all.css   
type .\resource\css\other.css               >> .\resource\common\obpm.main.all.css   

java -jar yuicompressor-2.4.8.jar .\resource\common\obpm.main.all.css -o .\resource\common\obpm.main.all.min.css


@echo off
for %%i in (".\resource\common\obpm.main.all.min.js")do (
  set str=%%~ti%%~zi)
set "str=%str:/=%"
set "str=%str: =%"
set "str=%str::=%"


set script="<script type='text/javascript' src='<s:url value='/portal/phone/resource/common/obpm.main.all.min.js?v=%str%'/>'></script>"
set css="<link rel='stylesheet' href='<s:url value='/portal/phone/resource/common/obpm.main.all.min.css?v=%str%'/>'/>"

set /p=%script%<nul > ./resource/common/include_main_all_js.jsp
set /p=%css%<nul > ./resource/common/include_main_all_css.jsp
