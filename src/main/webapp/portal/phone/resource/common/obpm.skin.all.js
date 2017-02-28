jQuery.extend({
	par2Json : function(string,overwrite){//param to json
		var obj = {}, pairs = string.split('&'), d = decodeURIComponent, name, value;
		jQuery.each(pairs, function(i, pair) {
			pair = pair.split('=');
			name = d(pair[0]);
			value = pair[1];
			obj[name] = overwrite || !obj[name] ? value : [].concat(obj[name])
					.concat(value);
		});
		return obj;
	},

	json2Par : function(o, pre) {//json to param
		var undef, buf = [], key, e = encodeURIComponent;
		for (key in o) {
			undef = o[key] == 'undefined';
			$.each(undef ? key : o[key], function(val, i) {
				buf.push("&", e(key), "=", (val != key || !undef) ? e(val) : "");
			});
		}
		if (!pre) {
			buf.shift();
			pre = "";
		}
		return pre + buf.join('');
	},
	
	obj2Str : function(o){//obj to str
		var r = [];
		if (typeof o == "string" || o == null) {
			return o;
		}
		if (typeof o == "object") {
			if (!o.sort) {
				r[0] = "{";
				for ( var i in o) {
					var str = HTMLEncode(jQuery.obj2Str(o[i]));
					
					r[r.length] = "\"" + i + "\"";
					r[r.length] = ":";
					r[r.length] = "\"" + str + "\"";
					r[r.length] = ",";
				}
				r[r.length - 1] = "}";
			} else {
				r[0] = "[";
				for ( var i = 0; i < o.length; i++) {
					r[r.length] = jQuery.obj2Str(o[i]);
					r[r.length] = ",";
				}
				r[r.length - 1] = "]";
			}
			return r.join("");
		}
		return o.toString();
	},
	json2Str: function(o) { //json数组转换成json字符串
        if (o == undefined) {
            return "";
        }
        var r = [];
        if (typeof o == "string") return "\"" + o.replace(/([\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
        if (typeof o == "object") {
            if (!o.sort) {
                for (var i in o)
                    r.push("\"" + i + "\":" + jQuery.json2Str(o[i]));
                if (navigator.userAgent.indexOf("MSIE 9.0")<0 && !!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
                    r.push("toString:" + o.toString.toString());
                }
                r = "{" + r.join() + "}";
            } else {
                for (var i = 0; i < o.length; i++)
                    r.push(jQuery.json2Str(o[i]));
                r = "[" + r.join() + "]";
            }
            return r;
        }
        return o.toString().replace(/\"\:/g, '":""');
    },
	  jQueryValues: function(object) {
	    var values = [];
	    for (var property in object)
	      values.push(object[property]);
	    return values;
	  }
});

jQuery.cachedScript = function( url, options ) {
 
  // Allow user to set any option except for dataType, cache, and url
  options = $.extend( options || {}, {
    dataType: "script",
    cache: true,
    url: url
  });
 
  // Use $.ajax() since it is more flexible than $.getScript
  // Return the jqXHR object so we can chain callbacks
  return jQuery.ajax( options );
};
 
; 
/*!
 * =====================================================
 * Ratchet v2.0.2 (http://goratchet.com)
 * Copyright 2014 Connor Sears
 * Licensed under MIT (https://github.com/twbs/ratchet/blob/master/LICENSE)
 *
 * v2.0.2 designed by @connors.
 * =====================================================
 */
!function(){"use strict";var a=function(a){for(var b,c=document.querySelectorAll("a");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},b=function(b){var c=a(b.target);return c&&c.hash?document.querySelector(c.hash):void 0};window.addEventListener("touchend",function(a){var c=b(a);c&&(c&&c.classList.contains("modal")&&c.classList.toggle("active"),a.preventDefault())})}(),!function(){"use strict";var a,b=function(a){for(var b,c=document.querySelectorAll("a");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},c=function(){a.style.display="none",a.removeEventListener("webkitTransitionEnd",c)},d=function(){var b=document.createElement("div");return b.classList.add("backdrop"),b.addEventListener("touchend",function(){a.addEventListener("webkitTransitionEnd",c),a.classList.remove("visible"),a.parentNode.removeChild(d)}),b}(),e=function(c){var d=b(c.target);if(d&&d.hash&&!(d.hash.indexOf("/")>0)){try{a=document.querySelector(d.hash)}catch(e){a=null}if(null!==a&&a&&a.classList.contains("popover"))return a}},f=function(a){var b=e(a);b&&(b.style.display="block",b.offsetHeight,b.classList.add("visible"),b.parentNode.appendChild(d))};window.addEventListener("touchend",f)}(),!function(){"use strict";var a,b=function(){},c=20,d=sessionStorage,e={},f={slideIn:"slide-out",slideOut:"slide-in",fade:"fade"},g={bartab:".bar-tab",barnav:".bar-nav",barfooter:".bar-footer",barheadersecondary:".bar-header-secondary"},h=function(a,b){o.id=a.id,b&&(a=k(a.id)),d[a.id]=JSON.stringify(a),window.history.replaceState(a.id,a.title,a.url),e[a.id]=document.body.cloneNode(!0)},i=function(){var a=o.id,b=JSON.parse(d.cacheForwardStack||"[]"),e=JSON.parse(d.cacheBackStack||"[]");for(e.push(a);b.length;)delete d[b.shift()];for(;e.length>c;)delete d[e.shift()];window.history.pushState(null,"",d[o.id].url),d.cacheForwardStack=JSON.stringify(b),d.cacheBackStack=JSON.stringify(e)},j=function(a,b){var c="forward"===b,e=JSON.parse(d.cacheForwardStack||"[]"),f=JSON.parse(d.cacheBackStack||"[]"),g=c?f:e,h=c?e:f;o.id&&g.push(o.id),h.pop(),d.cacheForwardStack=JSON.stringify(e),d.cacheBackStack=JSON.stringify(f)},k=function(a){return JSON.parse(d[a]||null)||{}},l=function(b){var c=t(b.target);if(!(!c||b.which>1||b.metaKey||b.ctrlKey||a||location.protocol!==c.protocol||location.host!==c.host||!c.hash&&/#/.test(c.href)||c.hash&&c.href.replace(c.hash,"")===location.href.replace(location.hash,"")||"push"===c.getAttribute("data-ignore")))return c},m=function(a){var b=l(a);b&&(a.preventDefault(),o({url:b.href,hash:b.hash,timeout:b.getAttribute("data-timeout"),transition:b.getAttribute("data-transition")}))},n=function(a){var b,c,h,i,l,m,n,p,q=a.state;if(q&&d[q]){if(l=o.id<q?"forward":"back",j(q,l),h=k(q),i=e[q],h.title&&(document.title=h.title),"back"===l?(n=JSON.parse("back"===l?d.cacheForwardStack:d.cacheBackStack),p=k(n[n.length-1])):p=h,"back"===l&&!p.id)return o.id=q;if(m="back"===l?f[p.transition]:p.transition,!i)return o({id:h.id,url:h.url,title:h.title,timeout:h.timeout,transition:m,ignorePush:!0});if(p.transition){h=v(h,".content",i.cloneNode(!0));for(b in g)g.hasOwnProperty(b)&&(c=document.querySelector(g[b]),h[b]?r(h[b],c):c&&c.parentNode.removeChild(c))}r((h.contents||i).cloneNode(!0),document.querySelector(".content"),m),o.id=q,document.body.offsetHeight}},o=function(a){var c,d=o.xhr;a.container=a.container||a.transition?document.querySelector(".content"):document.body;for(c in g)g.hasOwnProperty(c)&&(a[c]=a[c]||document.querySelector(g[c]));d&&d.readyState<4&&(d.onreadystatechange=b,d.abort()),d=new XMLHttpRequest,d.open("GET",a.url,!0),d.setRequestHeader("X-PUSH","true"),d.onreadystatechange=function(){a._timeout&&clearTimeout(a._timeout),4===d.readyState&&(200===d.status?p(d,a):q(a.url))},o.id||h({id:+new Date,url:window.location.href,title:document.title,timeout:a.timeout,transition:null}),a.timeout&&(a._timeout=setTimeout(function(){d.abort("timeout")},a.timeout)),d.send(),d.readyState&&!a.ignorePush&&i()},p=function(a,b){var c,d,e=w(a,b);if(!e.contents)return u(b.url);if(e.title&&(document.title=e.title),b.transition)for(c in g)g.hasOwnProperty(c)&&(d=document.querySelector(g[c]),e[c]?r(e[c],d):d&&d.parentNode.removeChild(d));r(e.contents,b.container,b.transition,function(){h({id:b.id||+new Date,url:e.url,title:e.title,timeout:b.timeout,transition:b.transition},b.id),s()}),!b.ignorePush&&window._gaq&&_gaq.push(["_trackPageview"]),!b.hash},q=function(a){throw new Error("Could not get: "+a)},r=function(a,b,c,d){var e,f,g;if(c?(e=/in$/.test(c),"fade"===c&&(b.classList.add("in"),b.classList.add("fade"),a.classList.add("fade")),/slide/.test(c)&&(a.classList.add("sliding-in",e?"right":"left"),a.classList.add("sliding"),b.classList.add("sliding")),b.parentNode.insertBefore(a,b)):b?b.innerHTML=a.innerHTML:a.classList.contains("content")?document.body.appendChild(a):document.body.insertBefore(a,document.querySelector(".content")),c||d&&d(),"fade"===c){b.offsetWidth,b.classList.remove("in");var h=function(){b.removeEventListener("webkitTransitionEnd",h),a.classList.add("in"),a.addEventListener("webkitTransitionEnd",i)},i=function(){a.removeEventListener("webkitTransitionEnd",i),b.parentNode.removeChild(b),a.classList.remove("fade"),a.classList.remove("in"),d&&d()};b.addEventListener("webkitTransitionEnd",h)}if(/slide/.test(c)){var j=function(){a.removeEventListener("webkitTransitionEnd",j),a.classList.remove("sliding","sliding-in"),a.classList.remove(g),b.parentNode.removeChild(b),d&&d()};b.offsetWidth,g=e?"right":"left",f=e?"left":"right",b.classList.add(f),a.classList.remove(g),a.addEventListener("webkitTransitionEnd",j)}},s=function(){var a=new CustomEvent("push",{detail:{state:k(o.id)},bubbles:!0,cancelable:!0});window.dispatchEvent(a)},t=function(a){for(var b,c=document.querySelectorAll("a");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},u=function(a){window.history.replaceState(null,"","#"),window.location.replace(a)},v=function(a,b,c){var d,e={};for(d in a)a.hasOwnProperty(d)&&(e[d]=a[d]);return Object.keys(g).forEach(function(a){var b=c.querySelector(g[a]);b&&b.parentNode.removeChild(b),e[a]=b}),e.contents=c.querySelector(b),e},w=function(a,b){var c,d,e={},f=a.responseText;if(e.url=b.url,!f)return e;/<html/i.test(f)?(c=document.createElement("div"),d=document.createElement("div"),c.innerHTML=f.match(/<head[^>]*>([\s\S.]*)<\/head>/i)[0],d.innerHTML=f.match(/<body[^>]*>([\s\S.]*)<\/body>/i)[0]):(c=d=document.createElement("div"),c.innerHTML=f),e.title=c.querySelector("title");var g="innerText"in e.title?"innerText":"textContent";return e.title=e.title&&e.title[g].trim(),b.transition?e=v(e,".content",d):e.contents=d,e};window.addEventListener("touchstart",function(){a=!1}),window.addEventListener("touchmove",function(){a=!0}),window.addEventListener("touchend",m),window.addEventListener("click",function(a){l(a)&&a.preventDefault()}),window.addEventListener("popstate",n),window.PUSH=o}(),!function(){"use strict";var a=function(a){for(var b,c=document.querySelectorAll(".segmented-control .control-item");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a};window.addEventListener("touchend",function(b){var c,d,e,f=a(b.target),g="active",h="."+g;if(f&&(c=f.parentNode.querySelector(h),c&&c.classList.remove(g),f.classList.add(g),f.hash&&(e=document.querySelector(f.hash)))){d=e.parentNode.querySelectorAll(h);for(var i=0;i<d.length;i++)d[i].classList.remove(g);e.classList.add(g)}}),window.addEventListener("click",function(b){a(b.target)&&b.preventDefault()})}(),!function(){"use strict";var a,b,c,d,e,f,g,h,i,j,k,l,m,n=function(a){for(var b,c=document.querySelectorAll(".slider > .slide-group");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a},o=function(){if("webkitTransform"in c.style){var a=c.style.webkitTransform.match(/translate3d\(([^,]*)/),b=a?a[1]:0;return parseInt(b,10)}},p=function(a){var b=a?0>d?"ceil":"floor":"round";k=Math[b](o()/(m/c.children.length)),k+=a,k=Math.min(k,0),k=Math.max(-(c.children.length-1),k)},q=function(f){if(c=n(f.target)){var k=c.querySelector(".slide");m=k.offsetWidth*c.children.length,l=void 0,j=c.offsetWidth,i=1,g=-(c.children.length-1),h=+new Date,a=f.touches[0].pageX,b=f.touches[0].pageY,d=0,e=0,p(0),c.style["-webkit-transition-duration"]=0}},r=function(h){h.touches.length>1||!c||(d=h.touches[0].pageX-a,e=h.touches[0].pageY-b,a=h.touches[0].pageX,b=h.touches[0].pageY,"undefined"==typeof l&&(l=Math.abs(e)>Math.abs(d)),l||(f=d/i+o(),h.preventDefault(),i=0===k&&d>0?a/j+1.25:k===g&&0>d?Math.abs(a)/j+1.25:1,c.style.webkitTransform="translate3d("+f+"px,0,0)"))},s=function(a){c&&!l&&(p(+new Date-h<1e3&&Math.abs(d)>15?0>d?-1:1:0),f=k*j,c.style["-webkit-transition-duration"]=".2s",c.style.webkitTransform="translate3d("+f+"px,0,0)",a=new CustomEvent("slide",{detail:{slideNumber:Math.abs(k)},bubbles:!0,cancelable:!0}),c.parentNode.dispatchEvent(a))};window.addEventListener("touchstart",q),window.addEventListener("touchmove",r),window.addEventListener("touchend",s)}(),!function(){"use strict";var a={},b=!1,c=!1,d=!1,e=function(a){for(var b,c=document.querySelectorAll(".toggle");a&&a!==document;a=a.parentNode)for(b=c.length;b--;)if(c[b]===a)return a};window.addEventListener("touchstart",function(c){if(c=c.originalEvent||c,d=e(c.target)){var f=d.querySelector(".toggle-handle"),g=d.clientWidth,h=f.clientWidth,i=d.classList.contains("active")?g-h:0;a={pageX:c.touches[0].pageX-i,pageY:c.touches[0].pageY},b=!1}}),window.addEventListener("touchmove",function(e){if(e=e.originalEvent||e,!(e.touches.length>1)&&d){var f=d.querySelector(".toggle-handle"),g=e.touches[0],h=d.clientWidth,i=f.clientWidth,j=h-i;if(b=!0,c=g.pageX-a.pageX,!(Math.abs(c)<Math.abs(g.pageY-a.pageY))){if(e.preventDefault(),0>c)return f.style.webkitTransform="translate3d(0,0,0)";if(c>j)return f.style.webkitTransform="translate3d("+j+"px,0,0)";f.style.webkitTransform="translate3d("+c+"px,0,0)",d.classList[c>h/2-i/2?"add":"remove"]("active")}}}),window.addEventListener("touchend",function(a){if(d){var e=d.querySelector(".toggle-handle"),f=d.clientWidth,g=e.clientWidth,h=f-g,i=!b&&!d.classList.contains("active")||b&&c>f/2-g/2;e.style.webkitTransform=i?"translate3d("+h+"px,0,0)":"translate3d(0,0,0)",d.classList[i?"add":"remove"]("active"),a=new CustomEvent("toggle",{detail:{isActive:i},bubbles:!0,cancelable:!0}),d.dispatchEvent(a),b=!1,d=!1}})}();; 
eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('(16($,f){16 88(a){1b i;1h(i 3g a){15(dD[a[i]]!==f){1a 1d}}1a 1e}16 dC(){1b a=[\'hu\',\'9l\',\'O\',\'1W\'],p;1h(p 3g a){15(88([a[p]+\'7I\'])){1a\'-\'+a[p].4u()+\'-\'}}1a\'\'}16 4C(a,b,c){1b d=a;15(5x b===\'aV\'){1a a.1n(16(){15(!1j.3P){1j.3P=\'2d\'+(++g)}15(3j[1j.3P]){3j[1j.3P].7A()}1k $.2d.2O[b.de||\'9m\'](1j,b)})}15(5x b===\'9T\'){a.1n(16(){1b r,8z=3j[1j.3P];15(8z&&8z[b]){r=8z[b].4F(1j,gR.7o.3p.3a(c,1));15(r!==f){d=r;1a 1e}}})}1a d}1b g=+1k 1p(),3j={},1V=$.1V,dD=5M.gJ(\'gH\').1L,3c=88([\'gG\',\'gF\',\'gE\',\'fD\',\'fd\']),4B=88([\'f9\',\'eP\',\'eN\']),6h=dC(),52=6h.1D(/^\\-/,\'\').1D(/\\-$/,\'\').1D(\'eK\',\'9l\');$.fn.2d=16(a){1V(1j,$.2d.b1);1a 4C(1j,a,cv)};$.2d=$.2d||{ej:\'2.14.4\',2m:{6h:6h,6K:52,3c:3c,4B:4B,5r:16(e,a){15(e.2g==\'4h\'){$(a).1J(\'1F-9P\',\'1\')}1i 15($(a).1J(\'1F-9P\')){$(a).44(\'1F-9P\');1a 1e}1a 1d},cg:16(a){1b b=[],i;1h(i 3g a){b.1l(a[i])}1a b},dR:16(a){1b b={},i;15(a){1h(i=0;i<a.1u;i++){b[a[i]]=a[i]}}1a b},a4:16(a){1a a-h2(a)>=0},5m:16(s){1a 5x s===\'9T\'},3C:16(e,c){1b a=e.bZ||e;1a a.bY?a.bY[0][\'aj\'+c]:e[\'aj\'+c]},aq:16(t,a){1b b=5Z.7W?7W(t[0]):t[0].1L,53,1O;15(3c){$.1n([\'t\',\'eC\',\'ei\',\'e3\',\'dX\'],16(i,v){15(b[v+\'bJ\']!==f){53=b[v+\'bJ\'];1a 1e}});53=53.2B(\')\')[0].2B(\', \');1O=a?(53[13]||53[5]):(53[12]||53[4])}1i{1O=a?b.2b.1D(\'1O\',\'\'):b.3F.1D(\'1O\',\'\')}1a 1O},3q:16(a,b,c){1a 1g.1G(b,1g.2c(a,c))}},5H:1e,6C:{4J:{},f5:{}},6y:{es:{},er:{}},8T:{},3j:3j,2O:{},b1:{},4f:{2W:\'2d\',5E:\'7B\'},5A:{},dV:16(o){1V(1j.5A,o)},8X:16(a,c,p){1j.b1[a]=16(s){1a 4C(1j,1V(s,{de:c,2S:p===1e?f:a}),cv)}}}})(3A);(16($,m,n,o){1b p,7E,1V=$.1V,1W=$.2d,3j=1W.3j,5A=1W.5A,2m=1W.2m,52=2m.6K,3c=2m.3c,3C=2m.3C,3q=2m.3q,5m=2m.5m,9w=/gM [1-3]/i.5y(fC.eE),9j=\'9n 89\',41=16(){},bi=16(a){a.2F()};1W.2O.9v=16(g,h,j){1b k,$4X,$9u,$1H,$b4,$5s,$4j,$3R,$9k,2v,1I,49,9i,3r,5g,6z,4P,7L,2S,9c,s,4O,7J,2W,9a,aN,aM,19=1j,$2G=$(g),aL=[],aK={};16 99(a){15(1I){1I.1q(\'1y-a\')}1I=$(1j);15(!1I.1B(\'1y-d\')&&!1I.1B(\'1y-98\')){1I.1m(\'1y-a\')}15(a.2g===\'2V\'){$(n).1r(\'39\',5d)}}16 5d(a){15(1I){1I.1q(\'1y-a\');1I=25}15(a.2g===\'39\'){$(n).2y(\'39\',5d)}}16 aI(a){15(a.6F==13){19.7G()}1i 15(a.6F==27){19.4c()}}16 6I(a){15(!a){$4j.4b()}19.7i(s.7i)}16 7h(a){1b b,5N,2g,4b=s.bL;$1H.6d();15(p&&!a){2A(16(){15(4b===o||4b===1d){7E=1d;b=p[0];2g=b.2g;5N=b.5N;ek{b.2g=\'3S\'}eo(ex){}p.4b();b.2g=2g;b.5N=5N}1i 15(4b){15(3j[$(4b).1J(\'3P\')]){1W.5H=1e}$(4b).4b()}},4p)}19.56=1e;2k(\'7h\',[])}16 7b(b){8h(aK[b.2g]);aK[b.2g]=2A(16(){1b a=b.2g==\'2N\';15(a&&!4O){1a}19.7a(!a)},4p)}16 6i(a){15(!1W.5H){15(a){a()}15($(n.bQ).6l(\'77,eF\')){$(n.bQ).eQ()}p=$2G;19.6i()}2A(16(){7E=1e},5k)}16 2k(a,b){1b c;b.1l(19);$.1n([5A,2W,2S,h],16(i,v){15(v&&v[a]){c=v[a].4F(g,b)}});1a c}19.7a=16(a){1b w,l,t,5O,aw,ah,ap,at,al,5R,5T,aZ,dh,2N,6t,4G,71=0,70=0,2M={},3J=1g.2c($3R[0].6u||$3R.6u(),$5s.2a()),3o=$3R[0].bR||$3R.bR();15((aN===3J&&aM===3o&&a)||9c){1a}15(19.6W||/2b|3O/.5y(s.28)){$4j.2a(3J)}15(2k(\'7b\',[$1H,3J,3o])===1e||!3r){1a}6t=$3R.eT();4G=$3R.bU();5O=s.5O===o?$2G:$(s.5O);15(19.7V&&s.6S!==\'61\'){15(3J<gI){$1H.1m(\'dw-8Y\')}1i{$1H.1q(\'dw-8Y\')}}15(!19.6W&&/8A|4R/.5y(s.28)){$9k.2a(\'\');$(\'.1v-w-p\',$1H).1n(16(){w=$(1j).5W(1d);71+=w;70=(w>70)?w:70});w=71>3J?70:71;$9k.2a(w).2M(\'ey-ez\',71>3J?\'\':\'eA\')}6z=19.6W?3J:$4j.5W();4P=19.6W?3o:$4j.4D(1d);4O=4P<=3o&&6z<=3J;19.4O=4O;15(s.28==\'8A\'){l=1g.1G(0,6t+(3J-6z)/2);t=4G+(3o-4P)/2}1i 15(s.28==\'4R\'){2N=1d;5R=$(\'.dw-5T-i\',$1H);ap=5O.2s();at=1g.2x($4X.2s().2b-ap.2b);al=1g.2x($4X.2s().3F-ap.3F);aw=5O.5W();ah=5O.4D();l=3q(al-($4j.5W(1d)-aw)/2,6t+3,6t+3J-6z-3);t=at-4P;15((t<4G)||(at>4G+3o)){$4j.1q(\'dw-4R-2b\').1m(\'dw-4R-3O\');t=at+ah}1i{$4j.1q(\'dw-4R-3O\').1m(\'dw-4R-2b\')}5T=5R.5W();aZ=3q(al+aw/2-(l+(6z-5T)/2),0,5T);$(\'.dw-5R\',$1H).2M({3F:aZ})}1i{l=6t;15(s.28==\'2b\'){t=4G}1i 15(s.28==\'3O\'){t=4G+3o-4P}}t=t<0?0:t;2M.2b=t;2M.3F=l;$4j.2M(2M);$5s.1P(0);dh=1g.1G(t+4P,s.5E==\'7B\'?$(n).1P():$4X[0].eD);$5s.2M({1P:dh});15(2N&&((t+4P>4G+3o)||(at>4G+3o))){9c=1d;2A(16(){9c=1e},5k);$3R.bU(1g.2c(t+4P-3o,dh-3o))}aN=3J;aM=3o};19.cD=16(b,c){aL.1l(b);15(s.28!==\'54\'){15(7J){b.1r(\'2V.dw\',16(a){a.2F()})}15(s.aH){b.1r(\'4b.dw\',16(){15(!7E){6i(c)}})}15(s.aD){19.2U(b,16(){6i(c)})}}};19.7G=16(){15(!3r||19.5Q(1e,\'4z\')!==1e){19.au();2k(\'eG\',[19.79])}};19.4c=16(){15(!3r||19.5Q(1e,\'4c\')!==1e){2k(\'cI\',[19.79])}};19.6f=16(){2k(\'cJ\',[$1H]);15(3r&&!19.4s){19.5Q(1e,\'6f\')}19.5h(25,1d)};19.eU=16(){s.3U=1e;15(19.68){$2G.8H(\'3U\',1e)}};19.gO=16(){s.3U=1d;15(19.68){$2G.8H(\'3U\',1d)}};19.6i=16(c,d){1b e;15(s.3U||19.56){1a}15(49!==1e){15(s.28==\'2b\'){49=\'gP\'}15(s.28==\'3O\'){49=\'gQ\'}}19.8D();2k(\'d5\',[]);e=\'<18 5g="\'+s.5g+\'" 1c="1v-\'+s.2W+(s.d6?\' 1v-\'+s.d6:\'\')+\' dw-\'+s.28+\' \'+(s.5J||\'\')+(19.7V?\' dw-8Y\':\'\')+(9w?\' 1v-gY\':\'\')+(9i?\'\':\' dw-h0\')+\'">\'+\'<18 1c="dw-5s">\'+(3r?\'<18 1c="d9"></18>\':\'\')+\'<18\'+(3r?\' 2L="h3" 3L="-1"\':\'\')+\' 1c="dw\'+(s.6s?\' dw-6s\':\' dw-h8\')+\'">\'+(s.28===\'4R\'?\'<18 1c="dw-5T"><18 1c="dw-5T-i"><18 1c="dw-5R"></18></18></18>\':\'\')+\'<18 1c="dk">\'+\'<18 1w-4s="hd" 1c="dw-1w dw-5B"></18>\'+(s.2T?\'<18 1c="ai">\'+(5m(s.2T)?s.2T:\'\')+\'</18>\':\'\')+\'<18 1c="dv">\';e+=19.aR();e+=\'</18>\';15(9i){e+=\'<18 1c="hC">\';$.1n(2v,16(i,b){b=5m(b)?19.2v[b]:b;15(b.3u===\'4z\'){b.a3=\'1y-s\'}15(b.3u===\'4c\'){b.a3=\'1y-c\'}b.3u=5m(b.3u)?19.8g[b.3u]:b.3u;e+=\'<18\'+(s.8c?\' 1L="2a:\'+(2r/2v.1u)+\'%"\':\'\')+\' 1c="hI \'+(b.a3||\'\')+\'"><18 3L="0" 2L="3S" 1c="1y\'+i+\' 1y-e \'+(b.5J===o?s.dH:b.5J)+(b.8Z?\' 1v-26 1v-26-\'+b.8Z:\'\')+\'">\'+(b.3v||\'\')+\'</18></18>\'});e+=\'</18>\'}e+=\'</18></18></18></18>\';$1H=$(e);$5s=$(\'.dw-5s\',$1H);$b4=$(\'.d9\',$1H);$9k=$(\'.dk\',$1H);$9u=$(\'.ai\',$1H);$4j=$(\'.dw\',$1H);k=$(\'.dw-1w\',$1H);19.dJ=$1H;19.dK=$9u;19.56=1d;7L=\'hS hT\';19.bd();2k(\'91\',[$1H]);15(3r){$(m).1r(\'64\',aI);15(s.4O){$1H.1r(\'62 dN dM\',16(a){15(4O){a.2F()}})}15(52!==\'9l\'){$(\'77,7G,3S\',$4X).1n(16(){15(!1j.3U){$(1j).1m(\'9A\').8H(\'3U\',1d)}})}7L+=\' 2N\';1W.dL=19;$1H.9h($4X);15(3c&&49&&!c){$1H.1m(\'dw-3g dw-7T\').1r(9j,16(){$1H.2y(9j).1q(\'dw-3g dw-7T\').2D(\'.dw\').1q(\'dw-\'+49);6I(d)}).2D(\'.dw\').1m(\'dw-\'+49)}}1i 15($2G.6l(\'18\')){$2G.22($1H)}1i{$1H.hR($2G)}2k(\'hQ\',[$1H]);19.7a();$3R.1r(7L,7b);$1H.1r(\'hP 2V\',bi).1r(\'3d\',\'.1y-e\',bi).1r(\'64\',\'.1y-e\',16(a){15(a.6F==32){a.2F();a.5K();$(1j).3d()}});2A(16(){$.1n(2v,16(i,b){19.2U($(\'.1y\'+i,$1H),16(a){b=5m(b)?19.2v[b]:b;b.3u.3a(1j,a,19)},1d)});15(s.dI){19.2U($b4,16(){19.4c()})}15(3r&&!49){6I(d)}$1H.1r(\'4h 2V\',\'.1y-e\',99).1r(\'4m\',\'.1y-e\',5d);19.ak($1H)},5k);2k(\'6I\',[$1H,19.4n])};19.5Q=16(a,b,c){15(!19.56||(!c&&!19.dG&&b==\'4z\')||(!c&&2k(\'hJ\',[19.4n,b])===1e)){1a 1e}15($1H){15(52!==\'9l\'){$(\'.9A\',$4X).1n(16(){$(1j).8H(\'3U\',1e).1q(\'9A\')})}15(3c&&3r&&49&&!a&&!$1H.1B(\'dw-7T\')){$1H.1m(\'dw-6o dw-7T\').2D(\'.dw\').1m(\'dw-\'+49).1r(9j,16(){7h(a)})}1i{7h(a)}$3R.2y(7L,7b)}15(3r){$(m).2y(\'64\',aI);6H 1W.dL}};19.7i=16(a){k.22(\'\');2A(16(){k.22(a)},2r)};19.9N=16(){1a 19.56};19.5h=41;19.aR=41;19.ak=41;19.8D=41;19.au=41;19.bd=41;19.9W=41;19.2U=16(c,d,e){1b f,a9,4e;15(s.2U){c.1r(\'4h.dw\',16(a){15(e){a.2F()}f=3C(a,\'X\');a9=3C(a,\'Y\');4e=1e}).1r(\'62.dw\',16(a){15(1g.2x(3C(a,\'X\')-f)>20||1g.2x(3C(a,\'Y\')-a9)>20){4e=1d}}).1r(\'4m.dw\',16(a){1b b=1j;15(!4e){a.2F();d.3a(b,a)}1W.5H=1d;2A(16(){1W.5H=1e},hH)})}c.1r(\'3d.dw\',16(a){15(!1W.5H){d.3a(1j,a)}a.2F()})};19.dF=16(a,b){1b c={};15(5x a===\'aV\'){c=a}1i{c[a]=b}19.4C(c)};19.7A=16(){19.5Q(1d,1e,1d);$.1n(aL,16(i,v){v.2y(\'.dw\')});15(19.68&&7J){g.9J=9a}2k(\'hG\',[]);6H 3j[g.3P];19=25};19.dE=16(){1a 19};19.2i=2k;19.4C=16(a){19.3h=s={};1V(h,a);1V(s,1W.4f,19.8U,5A,h);2W=1W.6y[s.2W]||1W.6y.2d;5g=1W.8T[s.5g];2k(\'dB\',[5g,h]);1V(s,2W,5g,5A,h);2S=1W.6C[19.dA][s.2S];s.2v=s.2v||(s.28!==\'54\'?[\'4z\',\'4c\']:[]);s.2T=s.2T===o?(s.28!==\'54\'?\'{5N}\':1e):s.2T;15(2S){2S=2S.3a(g,19);1V(s,2S,h)}15(!1W.6y[s.2W]){s.2W=\'2d\'}19.7V=(s.6S||(/2b|3O/.5y(s.28)?\'61\':\'\'))===\'61\';19.9W();$2G.2y(\'.dw\');49=9w?1e:s.hz;2v=s.2v;3r=s.28!==\'54\';7J=s.aH||s.aD;$3R=$(s.5E==\'7B\'?m:s.5E);$4X=$(s.5E);19.5E=$3R;19.4s=1d;$.1n(2v,16(i,b){15(b===\'4z\'||b.3u===\'4z\'){19.4s=1e;1a 1e}});19.2v.4z={3v:s.ax,3u:\'4z\'};19.2v.4c={3v:(19.4s)?s.aA:s.aC,3u:\'4c\'};19.2v.6f={3v:s.aE,3u:\'6f\'};19.68=$2G.6l(\'77\');9i=2v.1u>0;15(19.56){19.5Q(1d,1e,1d)}15(3r){19.8D();15(19.68&&7J){15(9a===o){9a=g.9J}g.9J=1d}19.cD($2G)}1i{19.6i()}$2G.1r(\'dz.dw\',16(){15(!19.aO){19.7O($2G.1M(),1d,1e)}19.aO=1e});2k(\'hx\',[])};19.2v={};19.8g={4z:19.7G,4c:19.4c,6f:19.6f};19.79=25;19.dG=1d;19.56=1e;15(!j){3j[g.3P]=19;19.4C(h)}};1W.2O.9v.7o.8U={5g:\'en\',ax:\'hw\',be:\'ht\',aA:\'hs\',aC:\'hr\',aE:\'hq\',3U:1e,dI:1d,aH:1d,aD:1d,28:\'8A\',4O:1d,2U:1d,dH:\'1y\',8c:1d,bL:1e};1W.6y.2d={6B:5,7U:1e,2T:1e,8c:1e,9R:1d,9S:1,47:\'ho\',82:\'2c\',du:\'dt-8b-ds-41\',ag:\'1v-26 1v-26-4k-dr\',as:\'1v-26 1v-26-4k-dq\',7D:\'1v-26 1v-26-4k-dp\',7C:\'1v-26 1v-26-4k-dn\'};$(m).1r(\'4b\',16(){15(p){7E=1d}});$(n).1r(\'he 39 2V 3d\',16(a){15(1W.5H){a.5K();a.2F();1a 1e}})})(3A,5Z,5M);(16($,n,q,r){1b u,1W=$.2d,2O=1W.2O,3j=1W.3j,2m=1W.2m,52=2m.6K,3c=2m.3c,4B=2m.4B,3C=2m.3C,3q=2m.3q,5r=2m.5r;2O.9m=16(f,g,h){1b m,1I,6q,2u,s,2i,3d,4e,1s,b6,4t,p,2c,1G,3D,2z,6x,7z,19=1j,$2G=$(f),5F={},6A={},9L={},2Y=[];16 d8(a){15(5r(a,1j)&&!u&&!3d&&!1I&&!86(1j)){a.2F();a.5K();u=1d;6q=s.87!=\'gZ\';3D=$(\'.dw-37\',1j);7u(3D);4e=5F[2z]!==r;p=4e?d7(3D):6A[2z];1s=3C(a,\'Y\');b6=1k 1p();4t=1s;2N(3D,2z,p,0.gV);15(6q){3D.48(\'.33\').1m(\'8j\')}15(a.2g===\'2V\'){$(q).1r(\'7r\',8m).1r(\'39\',8o)}}}16 8m(a){15(u){15(6q){a.2F();a.5K();4t=3C(a,\'Y\');15(1g.2x(4t-1s)>3||4e){2N(3D,2z,3q(p+(1s-4t)/2u,2c-1,1G+1));4e=1d}}}}16 8o(a){15(u){1b b=1k 1p()-b6,1M=3q(p+(1s-4t)/2u,2c-1,1G+1),8r,4w,8v,d3=3D.2s().2b;a.5K();15(3c&&b<5k){8r=(4t-1s)/b;4w=(8r*8r)/s.8B;15(4t-1s<0){4w=-4w}}1i{4w=4t-1s}8v=1g.2e(p-4w/2u);15(!4e){1b c=1g.4y((4t-d3)/2u),1C=$($(\'.dw-1C\',3D)[c]),3f=1C.1B(\'dw-v\'),hl=6q;15(2i(\'gN\',[1C])!==1e&&3f){8v=c}1i{hl=1d}15(hl&&3f){1C.1m(\'dw-hl\');2A(16(){1C.1q(\'dw-hl\')},2r)}}15(6q){7n(3D,8v,0,1d,1g.2e(1M))}15(a.2g===\'39\'){$(q).2y(\'7r\',8m).2y(\'39\',8o)}u=1e}}16 99(a){1I=$(1j);15(5r(a,1j)){3V(a,1I.48(\'.33\'),1I.1B(\'d2\')?b0:b3)}15(a.2g===\'2V\'){$(q).1r(\'39\',5d)}}16 5d(a){1I=25;15(3d){b5(7z);3d=1e}15(a.2g===\'39\'){$(q).2y(\'39\',5d)}}16 d1(a){15(a.6F==38){3V(a,$(1j),b3)}1i 15(a.6F==40){3V(a,$(1j),b0)}}16 cS(){15(3d){b5(7z);3d=1e}}16 cR(a){15(!86(1j)){a.2F();a=a.bZ||a;1b b=a.cQ?(a.cQ/fx):(a.cO?(-a.cO/3):0),t=$(\'.dw-37\',1j);7u(t);7n(t,1g.2e(6A[2z]-b),b<0?1:2)}}16 3V(a,w,b){a.5K();a.2F();15(!3d&&!86(w)&&!w.1B(\'8j\')){3d=1d;1b t=w.2D(\'.dw-37\');7u(t);b5(7z);7z=fc(16(){b(t)},s.cL);b(t)}}16 86(a){15($.7g(s.9g)){1b i=$(\'.33\',m).2z(a);1a s.9g[i]}1a s.9g}16 9H(i){1b a=\'<18 1c="dw-bf">\',w=2Y[i],l=1,7d=w.7d||[],21=w.21,1Q=w.1Q||21;$.1n(21,16(j,v){15(l%20===0){a+=\'</18><18 1c="dw-bf">\'}a+=\'<18 2L="dF" 1w-2C="1e" 1c="dw-1C dw-v" 1F-1M="\'+1Q[j]+\'"\'+(7d[j]?\' 1w-2R="\'+7d[j]+\'"\':\'\')+\' 1L="1P:\'+2u+\'1O;9s-1P:\'+2u+\'1O;">\'+\'<18 1c="dw-i"\'+(6x>1?\' 1L="9s-1P:\'+1g.2e(2u/6x)+\'1O;i9-eM:\'+1g.2e(2u/6x*0.8)+\'1O;"\':\'\')+\'>\'+v+\'</18></18>\';l++});a+=\'</18>\';1a a}16 7u(t){1b a=t.48(\'.33\').1B(\'9U\');2c=$(\'.dw-1C\',t).2z($(a?\'.dw-1C\':\'.dw-v\',t).eq(0));1G=1g.1G(2c,$(\'.dw-1C\',t).2z($(a?\'.dw-1C\':\'.dw-v\',t).eq(-1))-(a?s.6B-1:0));2z=$(\'.dw-37\',m).2z(t)}16 cG(v){1b t=s.2T;1a t?(5x t===\'16\'?t.3a(f,v):t.1D(/\\{5N\\}/i,v)):\'\'}16 d7(t){1a 1g.2e(-2m.aq(t,1d)/2u)}16 a1(t,i){8h(5F[i]);6H 5F[i];t.48(\'.33\').1q(\'8j\')}16 2N(t,a,b,c,d){1b e=-b*2u,1L=t[0].1L;15(e==9L[a]&&5F[a]){1a}9L[a]=e;15(3c){1L[52+\'81\']=2m.6h+\'cF \'+(c?c.eI(3):0)+\'s cE-6o\';1L[52+\'7I\']=\'85(0,\'+e+\'1O,0)\'}1i{1L.2b=e+\'1O\'}15(5F[a]){a1(t,a)}15(c&&d){t.48(\'.33\').1m(\'8j\');5F[a]=2A(16(){a1(t,a)},c*ad)}6A[a]=b}16 af(a,t,b,c){1b d=$(\'.dw-1C[1F-1M="\'+a+\'"]\',t),78=$(\'.dw-1C\',t),v=78.2z(d),l=78.1u;15(c){7u(t)}1i 15(!d.1B(\'dw-v\')){1b e=d,76=d,5b=0,5P=0;5a(v-5b>=0&&!e.1B(\'dw-v\')){5b++;e=78.eq(v-5b)}5a(v+5P<l&&!76.1B(\'dw-v\')){5P++;76=78.eq(v+5P)}15(((5P<5b&&5P&&b!==2)||!5b||(v-5b<0)||b==1)&&76.1B(\'dw-v\')){d=76;v=v+5P}1i{d=e;v=v-5b}}1a{3x:d,v:c?3q(v,2c,1G):v,1M:d.1B(\'dw-v\')?d.1J(\'1F-1M\'):25}}16 75(a,b,c,d,e){15(2i(\'aB\',[m,b,a,d])!==1e){$(\'.dw-37\',m).1n(16(i){1b t=$(1j),74=t.48(\'.33\').1B(\'9U\'),2h=i==b||b===r,8e=af(19.3X[i],t,d,74),3x=8e.3x;15(!(3x.1B(\'dw-2q\'))||2h){19.3X[i]=8e.1M;15(!74){$(\'.dw-2q\',t).44(\'1w-2C\');3x.1J(\'1w-2C\',\'1d\')}$(\'.dw-2q\',t).1q(\'dw-2q\');3x.1m(\'dw-2q\');2N(t,i,8e.v,2h?a:0.1,2h?e:1e)}});2i(\'cC\',[]);19.4n=s.5S(19.3X);15(19.4s){19.3t=c||19.3t;5h(c,c,0,1d)}19.dK.22(cG(19.4n));15(c){2i(\'eB\',[19.4n])}}}16 7n(t,a,b,c,d){a=3q(a,2c,1G);1b e=$(\'.dw-1C\',t).eq(a),o=d===r?a:d,73=d!==r,4W=2z,4w=1g.2x(a-o),2l=c?(a==o?0.1:4w*s.8n*1g.1G(0.5,(2r-4w)/2r)):0;19.3X[4W]=e.1J(\'1F-1M\');2N(t,4W,a,2l,73);2A(16(){75(2l,4W,1d,b,73)},10)}16 b0(t){1b a=6A[2z]+1;7n(t,a>1G?2c:a,1,1d)}16 b3(t){1b a=6A[2z]-1;7n(t,a<2c?1G:a,2,1d)}16 5h(a,b,c,d,e){15(19.56&&!d){75(c)}19.4n=s.5S(19.3X);15(!e){19.8q=19.3X.3p(0);19.79=19.3t?19.4n:25}15(a){2i(\'ep\',[19.3t?19.4n:\'\',b]);15(19.68){$2G.1M(19.3t?19.4n:\'\')}15(b){19.aO=1d;$2G.dz()}}}2O.9v.3a(1j,f,g,1d);19.7O=19.el=16(a,b,c,d,e){19.3t=a!==25&&a!==r;19.3X=$.7g(a)?a.3p(0):s.5U.3a(f,a,19)||[];5h(b,c===r?b:c,e,1e,d)};19.5V=19.eg=16(a){1b b=19.3t?19[a?\'4n\':\'79\']:25;1a 2m.a4(b)?+b:b};19.b7=19.7O;19.4T=16(a){1a a?19.3X:19.8q};19.5h=16(a,b,c,d,e){19.7O(a,b,e,d,c)};19.ee=19.4T;19.e9=16(b,c,d){15(m){1b i=0,4E=b.1u;$.1n(s.2Y,16(j,a){$.1n(a,16(k,w){15($.co(i,b)>-1){2Y[i]=w;$(\'.dw-37\',m).eq(i).22(9H(i));4E--;15(!4E){19.7a();75(c,r,d);1a 1e}}i++});15(!4E){1a 1e}})}};19.9z=af;19.aR=16(){1b b,22=\'\',l=0;$.1n(s.2Y,16(i,a){22+=\'<18 1c="1v-w-p 6T\'+(s.87!=\'4J\'?\' e6\':\' e4\')+(s.7U?\'\':\' e2\')+\'">\'+\'<18 1c="e1"\'+(s.8I?\'\':\' 1L="1G-2a:dZ;"\')+\'>\'+(4B?\'\':\'<6M 1c="dw-8M" ck="0" cj="0"><8S>\');$.1n(a,16(j,w){2Y[l]=w;b=w.2R!==r?w.2R:j;22+=\'<\'+(4B?\'18\':\'ch\')+\' 1c="dT"\'+\' 1L="\'+(s.9Q?(\'2a:\'+(s.9Q[l]||s.9Q)+\'1O;\'):(s.7j?(\'2c-2a:\'+(s.7j[l]||s.7j)+\'1O;\'):\'2c-2a:\'+s.2a+\'1O;\')+(s.8I?(\'1G-2a:\'+(s.8I[l]||s.8I)+\'1O;\'):\'\'))+\'">\'+\'<18 1c="33 33\'+l+(w.74?\' 9U\':\'\')+\'">\'+(s.87!=\'4J\'?\'<18 1c="1y-e 8W d2 \'+(s.ag||\'\')+\'" 1L="1P:\'+2u+\'1O;9s-1P:\'+2u+\'1O;"><1Z>+</1Z></18>\'+\'<18 1c="1y-e 8W dQ \'+(s.as||\'\')+\'" 1L="1P:\'+2u+\'1O;9s-1P:\'+2u+\'1O;"><1Z>&dP;</1Z></18>\':\'\')+\'<18 1c="hV">\'+b+\'</18>\'+\'<18 3L="0" 1w-4s="2y" 1w-2R="\'+b+\'" 2L="hL" 1c="hK">\'+\'<18 1c="hF" 1L="1P:\'+(s.6B*2u)+\'1O;">\'+\'<18 1c="dw-37" 1L="c9-2b:\'+(w.74?0:s.6B/2*2u-2u/2)+\'1O;">\';22+=9H(l)+\'</18></18><18 1c="hn"></18></18><18 1c="hj"\'+(s.9R?\' 1L="1P:\'+2u+\'1O;c9-2b:-\'+(2u/2+(s.9S||0))+\'1O;"\':\'\')+\'></18></18>\'+(4B?\'</18>\':\'</ch>\');l++});22+=(4B?\'\':\'</8S></6M>\')+\'</18></18>\'});1a 22};19.ak=16(a){a.1r(\'dM dN\',\'.33\',cR).1r(\'64\',\'.33\',d1).1r(\'c6\',\'.33\',cS).1r(\'4h 2V\',\'.33\',d8).1r(\'62\',\'.33\',8m).1r(\'4m\',\'.33\',8o).1r(\'4h 2V\',\'.8W\',99).1r(\'4m\',\'.8W\',5d)};19.bd=16(){m=19.dJ;75()};19.au=16(){19.3t=1d;5h(1d,1d,0,1d)};19.8D=16(){1b v=$2G.1M()||\'\';19.3t=v!==\'\';19.3X=19.8q?19.8q.3p(0):s.5U(v,19)||[];5h()};19.9W=16(){s=19.3h;2i=19.2i;2u=s.1P;6x=s.c5;19.7V=(s.6S||(/2b|3O/.5y(s.28)&&s.2Y.1u==1?\'61\':\'\'))===\'61\';15(6x>1){s.5J=(s.5J||\'\')+\' dw-h6\'}};19.h1={};15(!h){3j[f.3P]=19;19.4C(g)}};2O.9m.7o.dA=\'4J\';2O.9m.7o.8U=$.1V({},2O.9v.7o.8U,{7j:80,1P:40,6B:3,c5:1,cL:5k,9g:1e,7U:1d,2Y:[],87:\'4J\',2S:\'\',8B:0.gT,8n:0.gL,5S:16(d){1a d.ae(\' \')},5U:16(b,c){1b d=[],3Z=[],i=0,1Q;15(b!==25&&b!==r){d=(b+\'\').2B(\' \')}$.1n(c.3h.2Y,16(j,a){$.1n(a,16(k,w){1Q=w.1Q||w.21;15($.co(d[i],1Q)!==-1){3Z.1l(d[i])}1i{3Z.1l(1Q[0])}i++})});1a 3Z}})})(3A,5Z,5M);(16($){1b b=$.2d.6y,2W={28:\'3O\',47:\'gD\',6B:5,1P:34,7j:55,2T:1e,7U:1e,8c:1e,9R:1d,9S:1,fT:1d,fG:\'fE\',du:\'dt-8b-ds-41\',7D:\'1v-26 1v-26-4k-dp\',7C:\'1v-26 1v-26-4k-dn\',ag:\'1v-26 1v-26-4k-dr\',as:\'1v-26 1v-26-4k-dq\',dB:16(a,s){15(s.2W){s.2W=s.2W.1D(\'8b\',\'bX\')}}};b.bX=2W;b.8b=2W})(3A);(16(a,j){1b q=a.2d,t=q.2m,k=t.3q,h=t.6K,c=t.6h,f=t.3c,H=t.3C,w=t.aq,g=t.5r,n=t.a4,N=t.5m,b=a.1V,O=5Z.bW||16(a){a()},z=5Z.bV||16(){},G={8B:0.f7,8n:0.8,aF:"Y",bS:!0,7Y:!0};q.2O.6U=16(t,E,x){16 o(d){15(3A.2d.eS&&g(d,1j)&&!L&&("2V"==d.2g&&d.2F(),r&&r.1q("1v-1I-a"),s=!1,r=a(d.3D).48(".1v-1I",1j),r.1u&&!r.1B("1v-1I-d")&&(s=!0,K=2A(16(){r.1m("1v-1I-a")},2r)),L=!0,X=!1,aa.3m=i,D=H(d,"X"),3B=H(d,"Y"),$=D,p=T=A=0,ea=1k 1p,ca=+w(ba,fa)||0,J(ca,1),"2V"===d.2g))a(5M).1r("7r",l).1r("39",I)}16 l(a){15(L){$=H(a,"X");P=H(a,"Y");A=$-D;T=P-3B;p=fa?T:A;15(s&&(5<1g.2x(T)||5<1g.2x(A)))8h(K),r.1q("1v-1I-a"),s=!1;!X&&5<1g.2x(p)&&(aa.3m=!0,S||(S=!0,Q=O(C)));fa||(aa.3m?a.2F():7<1g.2x(T)&&(X=!0,ia.2i("4m")))}}16 C(){da.3Q&&da.9y&&(p=k(p,-d,d));J(k(ca+p,Y-y,F+y));S=!1}16 I(b){15(L){1b c;c=1k 1p-ea;z(Q);S=!1;15(!X&&aa.3m){da.bS&&f&&5k>c&&(c=p/c,p=1g.1G(1g.2x(p),c*c/da.8B)*(0>p?-1:1));da.3Q&&da.9y&&(p=k(p,-d,d));m=1g.2e((ca+p)/d);e=k(m*d,Y,F);15(R){15(0>p)1h(c=R.1u-1;0<=c;c--){15(1g.2x(e)+M>=R[c].9C){m=c;3y=2;e=R[c].bN;1A}}1i 15(0<=p)1h(c=0;c<R.1u;c++)15(1g.2x(e)<=R[c].9C){m=c;3y=1;e=R[c].bM;1A}e=k(e,Y,F)}J(e,da.2l||(2Q<Y||2Q>F?4p:1g.1G(4p,1g.2x(e-2Q)*da.8n)))}s&&(8h(K),r.1m("1v-1I-a"),2A(16(){r.1q("1v-1I-a")},2r),!X&&!aa.3m&&U("ec",[r]));"39"==b.2g&&a(5M).2y("7r",l).2y("39",I);L=!1}}16 J(a,d){2Q=a;ha=ba[0].1L;f?(ha[h+"7I"]="85("+(fa?"0,"+a+"1O,":a+"1O,0,")+"0)",ha[h+"81"]=c+"cF "+1g.2e(d||0)+"1W cE-6o"):ha[B]=a+"1O";i=!0;2A(16(){i=1e},d||0)}16 U(d,b){1b c;b.1l(aa);a.1n([E],16(a,e){e&&e[d]&&(c=e[d].4F(t,b))});1a c}1b r,K,M,A,T,p,B,e,y,$,P,s,F,Y,X,L,i,Q,S,d,R,ca,ea,D,3B,ha,ba,fa,aa=1j,2Q=0,m=0,3y=1,da=E,ia=a(t);aa.3m=!1;aa.2N=16(b,c){b=n(b)?1g.2e(b/d)*d:1g.9M((a(b,t).1u?1g.2e(ba.2s()[B]-a(b,t).2s()[B]):2Q)/d)*d;J(k(b,Y,F),c)};aa.51=16(){1b a;M=da.7k===j?fa?ia.1P():ia.2a():da.7k;Y=da.8u===j?fa?M-ba.1P():M-ba.2a():da.8u;F=da.7l===j?0:da.7l;!fa&&da.6s&&(a=F,F=-Y,Y=-a);N(da.3Q)&&(R=[],ba.2D(da.3Q).1n(16(){1b a=fa?1j.e8:1j.e7,d=fa?1j.e5:1j.dY;R.1l({9C:a+d/2,bM:-a,bN:M-a-d})}));d=n(da.3Q)?da.3Q:1;y=da.7Y?n(da.3Q)?d:n(da.7Y)?da.7Y:0:0;aa.2N(da.3Q?R?R[m]["3Q"+3y]:m*d:2Q)};aa.4C=16(a){aa.3h=da={};b(E,a);b(da,q.4f,G,E);B=(fa="Y"==da.aF)?"2b":"3F";ba=da.dS||ia.hU().eq(0);aa.51();ia.1r("4h 2V",o).1r("62",l).1r("4m 8C",I);t.bH&&t.bH("3d",16(a){aa.3m&&(a.5K(),a.2F())},!0)};aa.7A=16(){ia.2y("4h 2V",o).2y("62",l).2y("4m 8C",I);aa=25};aa.dE=16(){1a aa};x||aa.4C(E)};q.8X("hE","6U",!1)})(3A);(16(a,j,q,t){1b k=a.2d,h=k.6C.4J,c=k.2m,f=c.3c,H=c.6K,w=c.3C,g=c.5r,n="hB hp",N={5q:["2j"],7s:0,82:"hk",bG:hf,a7:1,bE:1,8L:!0,bD:!0,bC:!0,8O:!0,bx:!0,bw:"bv",bu:"1p",bt:"ef",an:"ed",bs:"dW",br:"bq av",bp:"bo av",bn:"bq 90",bm:"bo 90",7D:"1v-26 1v-26-4k-gC",7C:"1v-26 1v-26-4k-fy"};h.cz=16(b){16 O(b,d,e){1b c,f,i,g,h={},j=1S+7H;b&&a.1n(b,16(a,b){c=b.d||b.1s||b;f=c+"";15(b.1s&&b.2w)1h(g=1k 1p(b.1s);g<=b.2w;)i=1k 1p(g.1z(),g.1o(),g.1f()),h[i]=h[i]||[],h[i].1l(b),g.3n(g.1f()+1);1i 15(c.29)i=1k 1p(c.1z(),c.1o(),c.1f()),h[i]=h[i]||[],h[i].1l(b);1i 15(f.1x(/w/i)){1b k=+f.1D("w",""),l=0,p=u.1f(d,e-1S-2f,1).1X();1<u.7s-p+1&&(l=7);1h(F=0;F<5*3I;F++)i=u.1f(d,e-1S-2f,7*F-l-p+1+k),h[i]=h[i]||[],h[i].1l(b)}1i 15(f=f.2B("/"),f[1])11<=e+j&&(i=u.1f(d+1,f[0]-1,f[1]),h[i]=h[i]||[],h[i].1l(b)),1>=e-j&&(i=u.1f(d-1,f[0]-1,f[1]),h[i]=h[i]||[],h[i].1l(b)),i=u.1f(d,f[0]-1,f[1]),h[i]=h[i]||[],h[i].1l(b);1i 1h(F=0;F<3I;F++)i=u.1f(d,e-1S-2f+F,f[0]),h[i]=h[i]||[],h[i].1l(b)});1a h}16 z(a,d){7K=O(u.4N,a,d);bb=O(u.3f,a,d);b.aU(a,d)}16 G(a,b,d,e,c,i,f){1b g=\'<18 1c="dw-17-h dw-17-2h-c dw-17-\'+a+"-c "+(u.aW||"")+\'"><18 1c="dw-17-2h"><18 1c="dw-17-2h-p"><18 1c="dw-17-2h-8M"><18 1c="dw-17-2h-6r">\';1h(s=1;s<=b;s++)g=12>=s||s>d?g+\'<18 1c="dw-17-2h-m-3x dw-17-2h-3x dw-17-2h-41"><18 1c="dw-i">&aY;</18></18>\':g+(\'<18 3L="0" 2L="3S"\'+(i?\' 1w-2R="\'+i[s-13]+\'"\':"")+\' 1c="1y-e 1y-98 dw-17-2h-m-3x dw-17-2h-3x dw-17-\'+a+\'-s" 1F-1M=\'+(e+s-13)+\'><18 1c="dw-i dw-17-2h-8M"><18 1c="dw-17-2h-3x">\'+(f?f[s-13]:e+s-13+c)+"</18></18></18>"),s<b&&(0===s%12?g+=\'</18></18></18><18 1c="dw-17-2h-p" 1L="\'+(2K?"2b":$a?"gW":"3F")+":"+2r*1g.2e(s/12)+\'%"><18 1c="dw-17-2h-8M"><18 1c="dw-17-2h-6r">\':0===s%3&&(g+=\'</18><18 1c="dw-17-2h-6r">\'));1a g+"</18></18></18></18></18>"}16 v(d,e){1b c,i,f,g,h,j,k,l,p,s,m,D,ia,F,o=1,B=0;c=u.1f(d,e,1);1b y=u.1N(c),r=u.1o(c),R=25===u.7P&&!b.3t?25:b.1f(!0),n=u.1f(y,r,1).1X(),4L=\'<18 1c="dw-17-6M">\',q=\'<18 1c="dw-2P-4E-c">\';1<u.7s-n+1&&(B=7);1h(F=0;42>F;F++)ia=F+u.7s-B,c=u.1f(y,r,ia-n+1),i=c.1z(),f=c.1o(),g=c.1f(),h=u.1o(c),j=u.1X(c),D=u.9o(i,f),k=i+"-"+f+"-"+g,f=a.1V({3f:c<1k 1p(66.1z(),66.1o(),66.1f())||c>9q?!1:7K[c]===t||bb[c]!==t,2C:R&&R.1z()===i&&R.1o()===f&&R.1f()===g},b.b8(c,R)),l=f.3f,p=f.2C,i=f.5J,s=c.29()===(1k 1p).9r(0,0,0,0),m=h!==r,b9[k]=f,0===F%7&&(4L+=(F?"</18>":"")+\'<18 1c="dw-17-6r\'+(u.8L&&R&&0<=R-c&&eO>R-c?" dw-17-2P-hl":"")+\'">\',V&&("2t"==V&&m&&F?o=1==g?1:2:"2p"==V&&(o=u.bl(c)),q+=\'<18 1c="dw-2P-4E"><18 1c="dw-2P-4E-i">\'+o+"</18></18>",o++)),4L+=\'<18 2L="3S" 3L="-1" 1w-2R="\'+(s?u.bs+", ":"")+u.5o[c.1X()]+", "+u.3H[h]+" "+j+" "+(f.bh?", "+f.bh:"")+\'"\'+(m&&!eb?\' 1w-5B="1d"\':"")+(p?\' 1w-2C="1d"\':"")+(l?"":\' 1w-3U="1d"\')+\' 1F-1t="\'+ia%7+\'" 1F-4l="\'+k+\'"1c="dw-17-1t \'+(s?" dw-17-h9":"")+(u.hc||"")+(p?" dw-2q":"")+(i?" "+i:"")+(1==j?" dw-17-1t-hg":"")+(j==D?" dw-17-1t-hO":"")+(m?" dw-17-1t-94":"")+(l?" dw-17-1t-v 1y-e 1y-98":" dw-17-1t-eh")+\'"><18 1c="dw-i \'+(p?5i:"")+" "+(u.eR||"")+\'"><18 1c="dw-17-1t-fg">\'+j+"</18>"+(f.1H||"")+\'<18 1c="dw-17-1t-fu"></18></18></18>\';1a 4L+("</18>"+q+"</18></18>")}16 E(b,d,e){1b c=u.1f(b,d,1),f=u.1N(c),c=u.1o(c),i=f+93;15(4K){5e&&5e.1q("dw-2q").44("1w-2C").2D(".dw-i").1q(5i);cb&&cb.1q("dw-2q").44("1w-2C").2D(".dw-i").1q(5i);5e=a(\'.dw-17-2p-s[1F-1M="\'+f+\'"]\',L).1m("dw-2q").1J("1w-2C","1d");cb=a(\'.dw-17-2t-s[1F-1M="\'+c+\'"]\',L).1m("dw-2q").1J("1w-2C","1d");5e.2D(".dw-i").1m(5i);cb.2D(".dw-i").1m(5i);3N&&3N.2N(5e,e);a(".dw-17-2t-s",L).1q("1y-d");15(f===ia)1h(s=0;s<az;s++)a(\'.dw-17-2t-s[1F-1M="\'+s+\'"]\',L).1m("1y-d");15(f===4L)1h(s=ay+1;12>=s;s++)a(\'.dw-17-2t-s[1F-1M="\'+s+\'"]\',L).1m("1y-d")}1==3y.1u&&3y.1J("1w-2R",f).22(i);1h(s=0;s<1T;++s)c=u.1f(b,d-2f+s,1),f=u.1N(c),c=u.1o(c),i=f+93,a(2Q[s]).1J("1w-2R",u.3H[c]+(db?"":" "+f)).22((!db&&da<m?i+" ":"")+fa[c]+(!db&&da>m?" "+i:"")),1<3y.1u&&a(3y[s]).22(i);u.1f(b,d-2f-1,1)<3e?o(a(".dw-17-1Y-m",L)):x(a(".dw-17-1Y-m",L));u.1f(b,d+1T-2f,1)>3b?o(a(".dw-17-2E-m",L)):x(a(".dw-17-2E-m",L));u.1f(b,d,1).1z()<=3e.1z()?o(a(".dw-17-1Y-y",L)):x(a(".dw-17-1Y-y",L));u.1f(b,d,1).1z()>=3b.1z()?o(a(".dw-17-2E-y",L)):x(a(".dw-17-2E-y",L))}16 x(a){a.1q(ao).2D(".dw-17-1I-4Q").44("1w-3U")}16 o(a){a.1m(ao).2D(".dw-17-1I-4Q").1J("1w-3U","1d")}16 l(d,c){15(1U.2j&&("2j"===4a||c)){1b e,f,i=u.1f(ga,1K,1),g=1g.2x(12*(u.1N(d)-u.1N(i))+u.1o(d)-u.1o(i));b.7y&&g&&(ga=u.1N(d),1K=u.1o(d),d>i?(f=g>1S-2f+1T-1,1K-=f?0:g-1S,e="2E"):d<i&&(f=g>1S+2f,1K+=f?0:g-1S,e="1Y"),I.3a(1j,ga,1K,e,1g.2c(g,1S),f,!0));c||(b.2i("f3",[d]),u.8L&&(a(".dw-2q .dw-i",Q).1q(5i),a(".dw-2q",Q).1q("dw-2q").44("1w-2C"),a(".dw-17-2P-hl",Q).1q("dw-17-2P-hl"),(25!==u.7P||b.3t)&&a(\'.dw-17-1t[1F-4l="\'+d.1z()+"-"+d.1o()+"-"+d.1f()+\'"]\',Q).1m("dw-2q").1J("1w-2C","1d").2D(".dw-i").1m(5i).48(".dw-17-6r").1m("dw-17-2P-hl")));b.7y=!0}}16 C(a,d){z(a,d);1h(s=0;s<3I;s++)2o[s].22(v(a,d-2f-1S+s));b.8Q=!1}16 I(c,e,f,i,g,h,j){c&&8P.1l({y:c,m:e,by:f,bz:i,bA:g,73:h,bB:j});15(!4I){1b k=8P.gU(),c=k.y,e=k.m,f="2E"===k.by,i=k.bz,g=k.bA,h=k.73,j=k.bB||fb,k=u.1f(c,e,1),c=u.1N(k),e=u.1o(k);4I=!0;b.7w=!0;b.2i("bF",[c,e]);z(c,e);15(g)1h(s=0;s<1T;s++)2o[f?3I-1T+s:s].22(v(c,e-2f+s));h&&7v.1m("dw-17-57-a");2A(16(){b.7i(u.3H[e]+" "+c);E(c,e,4p);U(f?ca-d*i*5I:ca+d*i*5I,ba.1B("dw-17-v")?4p:0,16(){1b d;7v.1q("dw-17-57-a").1J("1w-5B","1d");15(f){d=2o.a2(0,i);1h(s=0;s<i;s++)2o.1l(d[s]),r(2o[2o.1u-1],+2o[2o.1u-2].1F("a0")+2r*5I)}1i{d=2o.a2(3I-i,i);1h(s=i-1;0<=s;s--)2o.7q(d[s]),r(2o[0],+2o[1].1F("a0")-2r*5I)}1h(s=0;s<i;s++)2o[f?3I-i+s:s].22(v(c,e-2f-1S+s+(f?3I-i:0))),g&&2o[f?s:3I-i+s].22(v(c,e-2f-1S+s+(f?0:3I-i)));1h(s=0;s<1T;s++)2o[1S+s].1m("dw-17-57-a").44("1w-5B");4I=!1;8P.1u?2A(16(){I()},10):(ga=c,1K=e,b.7w=!1,V&&ha.22(a(".dw-2P-4E-c",2o[1S]).22()),a(".dw-17-1t",L).1J("3L",-1),a(".dw-17-57-a .dw-17-1t",L).1J("3L",0),b.8Q&&K(),b.2i("9Y",[c,e]),j())})},10)}}16 J(){1b d=a(1j),c=b.4s,e=b.1f(!0),f=d.1J("1F-4l"),i=f.2B("-"),i=1k 1p(i[0],i[1],i[2]),e=1k 1p(i.1z(),i.1o(),i.1f(),e.3G(),e.5L(),e.67()),g=d.1B("dw-2q");15((eb||!d.1B("dw-17-1t-94"))&&!1!==b.2i("bI",[a.1V(b9[f],{2n:e,3x:1j,2C:g})]))b.7y=!1,ea=!0,b.3n(e,c,0.2,!c,!0),u.8O&&(3s=!0,i<u.1f(ga,1K-2f,1)?A():i>u.1f(ga,1K-2f+1T,0)&&M(),3s=!1)}16 U(a,b,c){1b e=S[0].1L,a=1g.1G(ca-d*1S,1g.2c(a,ca+d*1S));e[H+"81"]="6b "+(b||0)+"1W";15(f){15(c)15(R==a||!b)2A(c,4p);1i S.1r(n,16(){S.2y(n);e[H+"81"]="";c()});e[H+"7I"]="85("+(2K?"0,"+a+"1O,":a+"1O,0,")+"0)"}1i c&&2A(c,b||4p),e[2K?"2b":"3F"]=a+"1O";15(b||c)ca=a;R=a}16 r(a,d){a.1F("a0",d);f?a[0].1L[H+"7I"]="85("+(2K?"0,"+d+"%,":d+"%,0,")+"0)":a[0].1L[2K?"2b":"3F"]=d+"%"}16 K(){b.9N()&&1U.2j&&C(ga,1K)}16 M(){3s&&u.1f(ga,1K+1T-2f,1)<=3b&&I(ga,++1K,"2E",1,!1,!0,M)}16 A(){3s&&u.1f(ga,1K-2f-1,1)>=3e&&I(ga,--1K,"1Y",1,!1,!0,A)}16 T(a){3s&&u.1f(ga,1K,1)<=u.1f(u.1N(3b)-1,u.1o(3b)-7H,1)?I(++ga,1K,"2E",1S,!0,!0,16(){T(a)}):3s&&!a.1B("1y-d")&&I(u.1N(3b),u.1o(3b)-7H,"2E",1S,!0,!0)}16 p(a){3s&&u.1f(ga,1K,1)>=u.1f(u.1N(3e)+1,u.1o(3e)+2f,1)?I(--ga,1K,"1Y",1S,!0,!0,16(){p(a)}):3s&&!a.1B("1y-d")&&I(u.1N(3e),u.1o(3e)+2f,"1Y",1S,!0,!0)}16 B(a,d){a.1B("dw-17-v")||(a.1m("dw-17-v"+(d?"":" dw-17-p-3g")).1q("dw-17-p-6o dw-17-h"),b.2i("bK",[]))}16 e(a,d){a.1B("dw-17-v")&&a.1q("dw-17-v dw-17-p-3g").1m("dw-17-h"+(d?"":" dw-17-p-6o"))}16 y(a,d){(d||a).1B("dw-17-v")?e(a):B(a)}16 $(){a(1j).1q("dw-17-p-6o dw-17-p-3g")}1b P,s,F,Y,X,L,i,Q,S,d,R,ca,ea,D,3B,ha,ba,fa,aa,2Q,m,3y,da,ia,4L,az,ay,3e,3b,66,9q,4H,ga,1K,8y,8x,gb,8p,7f,7e,8l,bb,7K,5c,4a,8k,4I,3s,1T,3I,7H,2f,eb,3N,5e,cb,9B=1j,7v=[],2o=[],8P=[],1U={},b9={},fb=16(){},bO=a.1V({},b.3h),u=a.1V(b.3h,N,bO),bP="4l"==u.82?"":"2c"==u.82?"et":"eu",V=u.ew,7c=u.6S||(/2b|3O/.5y(u.28)?"61":""),4o="61"==7c&&"4R"!==u.28,bg="8A"==u.28,$a=u.6s,5I=$a?-1:1,bc=4o?25:u.eH,2K="eJ"==u.eL,4K=u.bx,1S=u.bE,db="bv"==u.bw,8d=u.5q.ae(","),5X=(!0===u.6X||!1!==u.6X&&4o)&&1<u.5q.1u,5Y=!5X&&u.6X===t&&!4o&&1<u.5q.1u,93=u.aG||"",5i=u.bT||"",hb="dw-2q "+(u.f8||""),7X=u.fr||"",ao="1y-d "+(u.ft||""),Z="",3i="";8d.1x(/2j/)?1U.2j=1:4K=!1;8d.1x(/2n/)&&(1U.2n=1);8d.1x(/2l/)&&(1U.2l=1);1U.2j&&1U.2n&&(5X=!0,5Y=!1);u.6S=7c;u.2S=(1U.2n||1U.2j?"2n":"")+(1U.2l?"2l":"");15("54"==u.28)a(1j).48(\'[1F-2L="aj"]\').1r("fz",16(){b.7a()});b.7w=!1;b.8Q=!1;b.7y=!0;b.b8=fb;b.aU=fb;b.9t=O;b.51=16(){b.7w?b.8Q=1d:K()};b.ar=16(a,d){1b c=b.9N();15(d&&c)l(a,1d);1i{ga=u.1N(a);1K=u.1o(a);15(c){E(ga,1K);C(ga,1K)}}};Y=h.1R.3a(1j,b);m=u.47.6D(/m/i);da=u.47.6D(/y/i);a.1V(Y,{7i:u.an,91:16(h){1b o,n,H="";L=h;i=u.28=="54"?a(1j).6l("18")?a(1j):a(1j).fX():b.5E;4H=b.1f(1d);15(!ga){ga=u.1N(4H);1K=u.1o(4H)}R=ca=0;3B=1d;4I=1e;fa=u.3H;4a="2j";15(u.4V){3e=1k 1p(u.4V.1z(),u.4V.1o(),1);66=u.4V}1i 66=3e=1k 1p(u.6P,0,1);15(u.4U){3b=1k 1p(u.4U.1z(),u.4U.1o(),1);9q=u.4U}1i 9q=3b=1k 1p(u.6O,11,31,23,59,59);h.1m("dw-2j"+(f?"":" dw-17-gS"));X=a(".dw",h);5c=a(".dv",h);1U.2n?1U.2n=a(".6T",L).eq(0):1U.2j&&a(".6T",L).eq(0).1m("6T-hh");15(1U.2l)1U.2l=a(".6T",L).eq(1);15(1U.2j){1T=u.a7=="c0"?1g.1G(1,1g.2c(3,1g.4y((bc||i[0].6u||i.6u())/c1))):u.a7;3I=1T+2*1S;7H=1g.4y(1T/2);2f=1g.2e(1T/2)-1;eb=u.c2===t?1T<2:u.c2;2K=2K&&1T<2;n=\'<18 1c="dw-17-9f"><18 1c="\'+($a?"dw-17-2E-m":"dw-17-1Y-m")+\' dw-17-1Y dw-17-1I 1y 1y-e"><18 2L="3S" 3L="0" 1c="dw-17-1I-4Q \'+(u.7D||"")+\'" 1w-2R="\'+u.br+\'"></18></18>\';1h(s=0;s<1T;++s)n=n+(\'<18 1c="dw-17-9f-m" 1L="2a: \'+2r/1T+\'%"><1Z 2L="3S" 1c="dw-17-2t"></1Z></18>\');n=n+(\'<18 1c="\'+($a?"dw-17-1Y-m":"dw-17-2E-m")+\' dw-17-2E dw-17-1I 1y 1y-e"><18 2L="3S" 3L="0" 1c="dw-17-1I-4Q \'+(u.7C||"")+\'" 1w-2R="\'+u.bp+\'"></18></18></18>\');db&&(H=\'<18 1c="dw-17-9f"><18 1c="\'+($a?"dw-17-2E-y":"dw-17-1Y-y")+\' dw-17-1Y dw-17-1I 1y 1y-e"><18 2L="3S" 3L="0" 1c="dw-17-1I-4Q \'+(u.7D||"")+\'" 1w-2R="\'+u.bn+\'"></18></18><1Z 2L="3S" 1c="dw-17-2p"></1Z><18 1c="\'+($a?"dw-17-1Y-y":"dw-17-2E-y")+\' dw-17-2E dw-17-1I 1y 1y-e"><18 2L="3S" 3L="0" 1c="dw-17-1I-4Q \'+(u.7C||"")+\'" 1w-2R="\'+u.bm+\'"></18></18></18>\');15(4K){ia=u.1N(3e);4L=u.1N(3b);az=u.1o(3e);ay=u.1o(3b);8l=1g.9M((4L-ia+1)/12)+2;Z=G("2t",36,24,0,"",u.3H,u.5l);3i=G("2p",8l*12,4L-ia+13,ia,93)}D=\'<18 1c="1v-w-p dw-17-c"><18 1c="dw-17 \'+(1T>1?" dw-17-h4 ":"")+(V?" dw-h5 ":"")+(eb?"":" dw-5Q-94 ")+(u.aW||"")+\'"><18 1c="dw-17-9u"><18 1c="dw-17-a8 \'+(db?"dw-17-a8-h7":"dw-17-a8-m")+\'">\'+(da<m||1T>1?H+n:n+H)+\'</18></18><18 1c="dw-17-7B"><18 1c="dw-17-m-c dw-17-v"><18 1c="dw-17-c3-c">\';1h(F=0;F<1T;++F){D=D+(\'<18 1w-5B="1d" 1c="dw-17-c3" 1L="2a: \'+2r/1T+\'%"><6M ck="0" cj="0"><8S>\');1h(s=0;s<7;s++)D=D+("<c4>"+u["5o"+bP][(s+u.7s)%7]+"</c4>");D=D+"</8S></6M></18>"}D=D+(\'</18><18 1c="dw-17-6L-c \'+(u.aW||"")+\'"><18 1c="dw-2P-a5-c \'+(u.hi||"")+\'"><18 1c="dw-2P-a5"></18></18><18 1c="dw-17-6L">\');1h(s=0;s<1T+2*1S;s++)D=D+\'<18 1c="dw-17-57" 1w-5B="1d"></18>\';D=D+("</18></18></18>"+Z+3i+"</18></18></18>");1U.2j=a(D)}a.1n(u.5q,16(d,b){1U[b]=a(\'<18 1c="dw-17-4g" 3P="\'+(9B.3P+"c7"+d)+\'"></18>\').c8(a(\'<18 1c="dw-17-4g-i"></18>\').c8(1U[b])).9h(5c)});o=\'<18 1c="dw-17-6X"><37 2L="hm">\';a.1n(u.5q,16(a,d){1U[d]&&(o=o+(\'<1C 2L="9b" 1w-5q="\'+(9B.3P+"c7"+a)+\'" 1c="dw-17-9b \'+(a?"":hb)+\'" 1F-97="\'+d+\'"><a hv="#" 1c="1y-e 1y-98 dw-i \'+(!a?7X:"")+\'">\'+u[d+"hy"]+"</a></1C>"))});o=o+"</37></18>";5c.hA(o);Q=a(".dw-17-6L-c",L);S=a(".dw-17-6L",L);15(1U.2j){7v=a(".dw-17-57",L).1n(16(d,b){2o.1l(a(b))});7v.3p(1S,1S+1T).1m("dw-17-57-a").44("1w-5B");1h(s=0;s<3I;s++)r(2o[s],2r*(s-1S)*5I);C(ga,1K);a(".dw-17-57-a .dw-17-1t",L).1J("3L",0);ha=a(".dw-2P-a5",L).22(a(".dw-2P-4E-c",2o[1S]).22())}2Q=a(".dw-17-2t",L);3y=a(".dw-17-2p",L);ba=a(".dw-17-m-c",L);15(4K){ba.1r("9n 89",$);Z=a(".dw-17-2t-c",L).1r("9n 89",$);3i=a(".dw-17-2p-c",L).1r("9n 89",$);a(".dw-17-2h-p",L);8y={aF:2K?"Y":"X",7k:0,3Q:0,7l:0,9y:1,6s:u.6s,2l:4p};3N=1k k.2O.6U(3i[0],8y);aa=1k k.2O.6U(Z[0],8y)}2A(16(){b.2U(Q,16(d){d=a(d.3D);15(!4I&&!7e){d=d.48(".dw-17-1t",1j);d.1B("dw-17-1t-v")&&J.3a(d[0])}});a(".dw-17-1I",L).1r("4h 2V 64",16(d){1b b=a(1j);15(d.2g!=="64"){d.2F();d=g(d,1j)}1i d=d.6F===32;15(!3s&&d&&!b.1B("1y-d")){3s=1d;b.1B("dw-17-1Y-m")?A():b.1B("dw-17-2E-m")?M():b.1B("dw-17-1Y-y")?p(b):b.1B("dw-17-2E-y")&&T(b);a(q).1r("39.cc",16(){a(q).2y(".cc");3s=1e})}}).1r("4m 8C c6",16(){3s=1e});a(".dw-17-9b",L).1r("4h 3d",16(d){1b c=a(1j);15(g(d,1j)&&!c.1B("dw-2q")){4a=c.1J("1F-97");a(".dw-17-4g",L).1q("dw-17-p-3g").1m("dw-17-4g-h");a(".dw-17-9b",L).1q(hb).44("1w-2C").2D(".dw-i").1q(7X);c.1m(hb).1J("1w-2C","1d").2D(".dw-i").1m(7X);1U[4a].1q("dw-17-4g-h").1m("dw-17-p-3g");15(4a==="2j"){P=b.1f(1d);(P.1z()!==4H.1z()||P.1o()!==4H.1o()||P.1f()!==4H.1f())&&l(P)}1i{4H=b.1f(1d);b.3n(4H,1e,0,1d)}15(4K){e(3i,1d);e(Z,1d);B(ba,1d)}b.2i("hD",[4a])}});15(4K){b.2U(a(".dw-17-2t",L),16(){3i.1B("dw-17-v")||y(ba);y(Z);e(3i)});b.2U(a(".dw-17-2p",L),16(){3i.1B("dw-17-v")||3N.2N(5e);Z.1B("dw-17-v")||y(ba);y(3i);e(Z)});b.2U(a(".dw-17-2t-s",L),16(){!aa.3m&&!a(1j).1B("1y-d")&&b.ar(u.1f(ga,a(1j).1J("1F-1M"),1))});b.2U(a(".dw-17-2p-s",L),16(){15(!3N.3m){P=u.1f(a(1j).1J("1F-1M"),1K,1);b.ar(1k 1p(c.3q(P,3e,3b)))}});b.2U(3i,16(){15(!3N.3m){e(3i);B(ba)}});b.2U(Z,16(){15(!aa.3m){e(Z);B(ba)}})}},5k);4o?h.1m("dw-17-8Y"):a(".dw-17",L).2a(bc||c1*1T);u.cd&&a(".dw-17-6L-c",L).1P(u.cd);15(u.bD){1b v,z,x,O,E,N,Y,K,ea,bb=u.bC,7K=j.bW||16(a){a()},7c=j.bV||fb,9Z=16(a){15(!ea&&!4I){8k=1d;7e=1e;ea=1d;O=1k 1p;ca=R;8x=w(a,"X");gb=w(a,"Y")}},5Y=16(){N=ea=1e;15(K){K=1e;7c(z);x=1e;v=2K?7f-gb:8p-8x;E=1k 1p-O;Y=(E<5k&&1g.2x(v)>50?v<0?-1S:1S:1g.2e((R-ca)/d))*5I;Y>0&&u.1f(ga,1K-Y-2f,1)>=3e?I(ga,1K-Y,"1Y",Y):Y<0&&u.1f(ga,1K-Y+1T-2f-1,1)<=3b?I(ga,1K-Y,"2E",-Y):bb&&U(ca,4p)}},cb=16(a){4I&&a.2F();15(ea){8p=w(a,"X");7f=w(a,"Y");v=2K?7f-gb:8p-8x;15(!K&&!N)15(1g.2x(v)>7)7e=K=1d;1i 15(!b.4O&&!2K&&1g.2x(7f-gb)>10){7e=N=1d;a.2g==="62"&&Q.2i("4m")}K&&a.2F();15(K&&bb&&!x){x=1d;z=7K(5X)}}},5X=16(){U(ca+v);x=1e};Q.1r("4h",9Z).1r("62",cb).1r("4m 8C",5Y).1r("2V",16(d){15(!8k){9Z(d);a(q).1r("7r.9X",cb).1r("39.9X",16(){5Y();a(q).2y(".9X")})}8k=1e})}},6I:16(){15(1U.2j){E(ga,1K);b.2i("9Y",[ga,1K])}},7b:16(c,e,f){1b g,h,j,k=0,l=0,p=0;15(4o){bg&&Q.1P("");5c.1P("");S.2a("")}d&&(j=d);15(d=1g.2e(1g.2e(96(Q.2M(2K?"1P":"2a")))/1T)){L.1q("1v-17-m 1v-17-l");d>hM?L.1m("1v-17-l"):d>hN&&L.1m("1v-17-m")}15(5X&&(3B||4o)||5Y){a(".dw-17-4g",L).1q("dw-17-4g-h");a.1n(1U,16(a,d){g=d.5W();k=1g.1G(k,g);l=1g.1G(l,d.4D());p=p+g});15(5X||5Y&&p>(i[0].6u||i.6u())){h=1d;4a=a(".dw-17-6X .dw-2q",L).1J("1F-97");X.1m("dw-17-ce")}1i{4a="2j";l=k="";X.1q("dw-17-ce");5c.2M({2a:"",1P:""})}}15(4o&&bg){b.6W=1d;h&&5c.1P(1U.2j.4D());c=X.4D();f>=c&&Q.1P(f-c+Q.4D());l=1g.1G(l,1U.2j.4D())}15(h){5c.2M({2a:4o?"":k,1P:l});d=1g.2e(1g.2e(96(Q.2M(2K?"1P":"2a")))/1T)}15(d){15(db){fa=u.bG>a(".dw-17-9f-m",L).2a()?u.5l:u.3H;1h(s=0;s<1T;++s)a(2Q[s]).3v(fa[u.1o(u.1f(ga,1K-2f+s,1))])}S[2K?"1P":"2a"](d);15(4K){f=3i[2K?"1P":"2a"]();a.1V(3N.3h,{7k:f,3Q:f,8u:(2-8l)*f,7l:-f});a.1V(aa.3h,{7k:f,3Q:f,8u:-f,7l:-f});3N.51();aa.51();3i.1B("dw-17-v")&&3N.2N(5e)}15(4o&&!3B&&j){j=ca/j;ca=j*d;U(ca,0)}}15(h){a(".dw-17-4g",L).1m("dw-17-4g-h");1U[4a].1q("dw-17-4g-h")}b.2i("cf",[]);3B=1e},7h:16(){2o=[];1K=ga=4a=25;4I=1d;15(4K&&3N&&aa){3N.7A();aa.7A()}},cC:16(){1b a,d,c;d=b.1f(1d);15(ea)a="2j";1i 1h(c 3g b.95)c&&b.95[c]===s&&(a=/hW/.5y(c)?"2n":"2l");b.2i("hX",[{2n:d,97:a}]);l(d);ea=1e}});1a Y}})(3A,5Z,5M);(16(a,j){1b q=a.2d,t=q.1R,k=1k 1p,h={6P:k.1z()-2r,6O:k.1z()+1,92:" ",63:"3z/dd/2Z",47:"dU",6J:"ci",4i:"hh:43 A",6N:"cl",7R:"90",6Q:"cm",6R:"cn",8E:"&aY;",6V:"cp",6Y:"cq"},c=16(c){16 k(a,d,b){1a s[d]!==j?+a[s[d]]:b!==j?b:F[d](ca)}16 w(a,d,b,c){a.1l({21:b,1Q:d,2R:c})}16 g(a,d,b,c){1a 1g.2c(c,1g.4y(a/d)*d+b)}16 n(a){15(25===a)1a a;1b d=k(a,"h",0);1a e.1f(k(a,"y"),k(a,"m"),k(a,"d",1),k(a,"a",0)?d+12:d,k(a,"i",0),k(a,"s",0))}16 N(a,d){1b c,e,i=!1,f=!1,g=0,h=0;15(b(a))1a a;a<ha&&(a=ha);a>ba&&(a=ba);e=c=a;15(2!==d)1h(i=b(c);!i&&c<ba;)c=1k 1p(c.29()+cr),i=b(c),g++;15(1!==d)1h(f=b(e);!f&&e>ha;)e=1k 1p(e.29()-cr),f=b(e),h++;1a 1===d&&i?c:2===d&&f?e:h<g&&f?e:c}16 b(a){1a a<ha||a>ba?!1:O(a,X)?!0:O(a,Y)?!1:!0}16 O(a,d){1b b,c,e;15(d)1h(c=0;c<d.1u;c++)15(b=d[c],e=b+"",!b.1s)15(b.29){15(a.1z()==b.1z()&&a.1o()==b.1o()&&a.1f()==b.1f())1a!0}1i 15(e.1x(/w/i)){15(e=+e.1D("w",""),e==a.1X())1a!0}1i 15(e=e.2B("/"),e[1]){15(e[0]-1==a.1o()&&e[1]==a.1f())1a!0}1i 15(e[0]==a.1f())1a!0;1a!1}16 z(a,d,b,c,i,f,g){1b h,j,k;15(a)1h(h=0;h<a.1u;h++)15(j=a[h],k=j+"",!j.1s)15(j.29)e.1N(j)==d&&e.1o(j)==b&&(f[e.1X(j)-1]=g);1i 15(k.1x(/w/i)){k=+k.1D("w","");1h(U=k-c;U<i;U+=7)0<=U&&(f[U]=g)}1i k=k.2B("/"),k[1]?k[0]-1==b&&(f[k[1]-1]=g):f[k[0]-1]=g}16 G(b,c,i,f,h,k,l,s,m){1b o,F,R,n,y,r,q,t,z,A,H,ca,x,w,G,P,da,O,C={},S={h:ea,i:D,s:3B,a:1},L=e.1f(h,k,l),Q=["a","h","i","s"];b&&(a.1n(b,16(a,d){15(d.1s&&(d.4F=!1,o=d.d,F=o+"",R=F.2B("/"),o&&(o.29&&h==e.1N(o)&&k==e.1o(o)&&l==e.1X(o)||!F.1x(/w/i)&&(R[1]&&l==R[1]&&k==R[0]-1||!R[1]&&l==R[0])||F.1x(/w/i)&&L.1X()==+F.1D("w",""))))d.4F=!0,C[L]=!0}),a.1n(b,16(b,e){H=w=x=0;ca=j;q=r=!0;G=!1;15(e.1s&&(e.4F||!e.d&&!C[L])){n=e.1s.2B(":");y=e.2w.2B(":");1h(A=0;3>A;A++)n[A]===j&&(n[A]=0),y[A]===j&&(y[A]=59),n[A]=+n[A],y[A]=+y[A];n.7q(11<n[0]?1:0);y.7q(11<y[0]?1:0);d&&(12<=n[1]&&(n[1]-=12),12<=y[1]&&(y[1]-=12));1h(A=0;A<c;A++)15($[A]!==j){t=g(n[A],S[Q[A]],p[Q[A]],B[Q[A]]);z=g(y[A],S[Q[A]],p[Q[A]],B[Q[A]]);O=da=P=0;d&&1==A&&(P=n[0]?12:0,da=y[0]?12:0,O=$[0]?12:0);r||(t=0);q||(z=B[Q[A]]);15((r||q)&&t+P<$[A]+O&&$[A]+O<z+da)G=!0;$[A]!=t&&(r=!1);$[A]!=z&&(q=!1)}15(!m)1h(A=c+1;4>A;A++)0<n[A]&&(x=S[i]),y[A]<B[Q[A]]&&(w=S[i]);G||(t=g(n[c],S[i],p[i],B[i])+x,z=g(y[c],S[i],p[i],B[i])-w,r&&(H=0>t?0:t>B[i]?a(".dw-1C",s).1u:v(s,t)+0),q&&(ca=0>z?0:z>B[i]?a(".dw-1C",s).1u:v(s,z)+1));15(r||q||G)m?a(".dw-1C",s).3p(H,ca).1m("dw-v"):a(".dw-1C",s).3p(H,ca).1q("dw-v")}}))}16 v(d,b){1a a(".dw-1C",d).2z(a(\'.dw-1C[1F-1M="\'+b+\'"]\',d))}16 E(a){1b d,b=[];15(25===a||a===j)1a a;1h(d 3g s)b[s[d]]=F[d](a);1a b}16 x(a){1b d,b,c,e=[];15(a){1h(d=0;d<a.1u;d++)15(b=a[d],b.1s&&b.1s.29)1h(c=1k 1p(b.1s);c<=b.2w;)e.1l(1k 1p(c.1z(),c.1o(),c.1f())),c.3n(c.1f()+1);1i e.1l(b);1a e}1a a}1b o=a(1j),l={},C;15(o.6l("77")){8w(o.1J("2g")){1E"2n":C="2Z-3z-dd";1A;1E"1R":C="2Z-3z-8t:43:cs";1A;1E"1R-ct":C="2Z-3z-8t:43:3W";1A;1E"2t":C="2Z-3z";l.47="cu";1A;1E"2l":C="b2:43:3W"}1b I=o.1J("2c"),o=o.1J("1G");I&&(l.4V=t.4q(C,I));o&&(l.4U=t.4q(C,o))}1b J,U,r,K,M,A,T,p,B,I=a.1V({},c.3h),e=a.1V(c.3h,q.1R.4f,h,l,I),y=0,$=[],l=[],P=[],s={},F={y:16(a){1a e.1N(a)},m:16(a){1a e.1o(a)},d:16(a){1a e.1X(a)},h:16(a){a=a.3G();a=d&&12<=a?a-12:a;1a g(a,ea,fa,m)},i:16(a){1a g(a.5L(),D,aa,3y)},s:16(a){1a g(a.67(),3B,2Q,da)},a:16(a){1a S&&11<a.3G()?1:0}},Y=e.4N,X=e.3f,I=e.2S,L=e.47,i=e.6J,Q=L.1x(/D/),S=i.1x(/a/i),d=i.1x(/h/),R="1R"==I?e.63+e.92+e.4i:"2l"==I?e.4i:e.63,ca=1k 1p,o=e.3l||{},ea=o.cw||e.cx||1,D=o.cy||e.bk||1,3B=o.cA||e.cB||1,o=o.6v,ha=e.4V||1k 1p(e.6P,0,1),ba=e.4U||1k 1p(e.6O,11,31,23,59,59),fa=o?0:ha.3G()%ea,aa=o?0:ha.5L()%D,2Q=o?0:ha.67()%3B,m=1g.4y(((d?11:23)-fa)/ea)*ea+fa,3y=1g.4y((59-aa)/D)*D+aa,da=1g.4y((59-aa)/D)*D+aa;C=C||R;15(I.1x(/2n/i)){a.1n(["y","m","d"],16(a,d){J=L.6D(6Z(d,"i"));-1<J&&P.1l({o:J,v:d})});P.72(16(a,d){1a a.o>d.o?1:-1});a.1n(P,16(a,d){s[d.v]=a});o=[];1h(U=0;3>U;U++)15(U==s.y){y++;K=[];r=[];M=e.1N(ha);A=e.1N(ba);1h(J=M;J<=A;J++)r.1l(J),K.1l((L.1x(/2Z/i)?J:(J+"").8i(2,2))+(e.aG||""));w(o,r,K,e.7R)}1i 15(U==s.m){y++;K=[];r=[];1h(J=0;12>J;J++)M=L.1D(/[dy]/gi,"").1D(/3z/,(9>J?"0"+(J+1):J+1)+(e.8f||"")).1D(/m/,J+1+(e.8f||"")),r.1l(J),K.1l(M.1x(/6n/)?M.1D(/6n/,\'<1Z 1c="dw-84">\'+e.3H[J]+"</1Z>"):M.1D(/M/,\'<1Z 1c="dw-84">\'+e.5l[J]+"</1Z>"));w(o,r,K,e.83)}1i 15(U==s.d){y++;K=[];r=[];1h(J=1;32>J;J++)r.1l(J),K.1l((L.1x(/dd/i)&&10>J?"0"+J:J)+(e.6k||""));w(o,r,K,e.6N)}l.1l(o)}15(I.1x(/2l/i)){T=!0;P=[];a.1n(["h","i","s","a"],16(a,d){a=i.6D(6Z(d,"i"));-1<a&&P.1l({o:a,v:d})});P.72(16(a,d){1a a.o>d.o?1:-1});a.1n(P,16(a,d){s[d.v]=y+a});o=[];1h(U=y;U<y+4;U++)15(U==s.h){y++;K=[];r=[];1h(J=fa;J<(d?12:24);J+=ea)r.1l(J),K.1l(d&&0===J?12:i.1x(/hh/i)&&10>J?"0"+J:J);w(o,r,K,e.6Q)}1i 15(U==s.i){y++;K=[];r=[];1h(J=aa;60>J;J+=D)r.1l(J),K.1l(i.1x(/43/)&&10>J?"0"+J:J);w(o,r,K,e.6R)}1i 15(U==s.s){y++;K=[];r=[];1h(J=2Q;60>J;J+=3B)r.1l(J),K.1l(i.1x(/3W/)&&10>J?"0"+J:J);w(o,r,K,e.6V)}1i U==s.a&&(y++,I=i.1x(/A/),w(o,[0,1],I?[e.3T.6j(),e.3E.6j()]:[e.3T,e.3E],e.8E));l.1l(o)}c.5V=16(a){1a c.3t||a?n(c.4T(a)):25};c.3n=16(a,d,b,e,i){c.b7(E(a),d,i,e,b)};c.1f=c.5V;c.2J=R;c.95=s;c.8g.6g=16(){c.3n(1k 1p,!1,0.3,!0,!0)};c.2v.6g={3v:e.6Y,3u:"6g"};Y=x(Y);X=x(X);ha=n(E(ha));ba=n(E(ba));p={y:ha.1z(),m:0,d:1,h:fa,i:aa,s:2Q,a:0};B={y:ba.1z(),m:11,d:31,h:m,i:3y,s:da,a:1};1a{2Y:l,2T:e.2T?16(){1a t.3Y(R,n(c.4T(!0)),e)}:!1,5S:16(a){1a t.3Y(C,n(a),e)},5U:16(a){1a E(a?t.4q(C,a,e):e.7P||1k 1p)},aB:16(d,b,i,g){1b b=N(n(c.4T(!0)),g),h=E(b),l=k(h,"y"),m=k(h,"m"),D=!0,o=!0;a.1n("y,m,d,a,h,i,s".2B(","),16(b,c){15(s[c]!==j){1b i=p[c],f=B[c],g=31,n=k(h,c),y=a(".dw-37",d).eq(s[c]);15(c=="d"){f=g=e.9o(l,m);Q&&a(".dw-1C",y).1n(16(){1b d=a(1j),b=d.1F("1M"),c=e.1f(l,m,b).1X(),b=L.1D(/[cH]/gi,"").1D(/dd/,(b<10?"0"+b:b)+(e.6k||"")).1D(/d/,b+(e.6k||""));a(".dw-i",d).22(b.1x(/9p/)?b.1D(/9p/,\'<1Z 1c="dw-1t">\'+e.5o[c]+"</1Z>"):b.1D(/D/,\'<1Z 1c="dw-1t">\'+e.6e[c]+"</1Z>"))})}D&&ha&&(i=F[c](ha));o&&ba&&(f=F[c](ba));15(c!="y"){1b R=v(y,i),r=v(y,f);a(".dw-1C",y).1q("dw-v").3p(R,r+1).1m("dw-v");c=="d"&&a(".dw-1C",y).1q("dw-h").3p(g).1m("dw-h")}n<i&&(n=i);n>f&&(n=f);D&&(D=n==i);o&&(o=n==f);15(c=="d"){i=e.1f(l,m,1).1X();f={};z(Y,l,m,i,g,f,1);z(X,l,m,i,g,f,0);a.1n(f,16(d,b){b&&a(".dw-1C",y).eq(d).1q("dw-v")})}}});T&&a.1n(["a","h","i","s"],16(b,e){1b i=k(h,e),p=k(h,"d"),D=a(".dw-37",d).eq(s[e]);s[e]!==j&&(G(Y,b,e,h,l,m,p,D,0),G(X,b,e,h,l,m,p,D,1),$[b]=+c.9z(i,D,g).1M)});c.3X=h}}};a.1n(["2n","2l","1R"],16(a,h){q.6C.4J[h]=c})})(3A);(16(a,j,q,t){1b k=a.2d,h=a.1V,c=k.2m,f=k.1R,H=k.6C.4J,w={cK:0,9K:"eV,eW,eX,eY,eZ,f0".2B(","),9I:"f4",9E:"f6",cM:"2k",cN:"2I"};k.8X("2j");H.2j=16(g){16 n(d){15(d){15(e[d])1a e[d];1b b=a(\'<18 1L="7m-3K:\'+d+\';"></18>\').9h("7B"),c=(j.7W?7W(b[0]):b[0].1L).fe.1D(/ff|fh|\\(|\\)|\\s/g,"").2B(","),c=fi<0.fj*c[0]+0.fk*c[1]+0.fl*c[2]?"#fm":"#fo";b.6d();1a e[d]=c}}16 q(a){1a a.72(16(a,d){1b b=a.d||a.1s,c=d.d||d.1s,b=!b.29?0:a.1s&&a.2w&&a.1s.6a()!==a.2w.6a()?1:b.29(),c=!c.29?0:d.1s&&d.2w&&d.1s.6a()!==d.2w.6a()?1:c.29();1a b-c})}16 b(d){1b b;b=a(".dw-17-c",o).4D();1b c=d.4D(),e=d.5W(),f=d.2s().2b-a(".dw-17-c",o).2s().2b,i=2>d.48(".dw-17-6r").2z();b=l.1m("dw-17-2I-t").2M({2b:i?f+c:"0",3O:i?"0":b-f}).1m("dw-17-2I-v").1P();l.2M(i?"3O":"2b","c0").1q("dw-17-2I-t");U.2M("1G-1P",b);J.51();J.2N(0);i?l.1m("dw-17-2I-b"):l.1q("dw-17-2I-b");a(".dw-17-2I-5R",l).2M("3F",d.2s().3F-l.2s().3F+e/2)}16 O(d,c){1b e=I[d];15(e){1b f,i,h,j,o,p=\'<37 1c="dw-17-2k-fp">\';C=c;c.1m($).2D(".dw-i").1m(s);c.1B(P)&&c.1J("1F-hl","1d").1q(P);q(e);a.1n(e,16(a,b){j=b.d||b.1s;o=b.1s&&b.2w&&b.1s.6a()!==b.2w.6a();h=b.3K;n(h);i=f="";j.29&&(f=k.1R.3Y((o?"6n d 2Z ":"")+y.4i,j));b.2w&&(i=k.1R.3Y((o?"6n d 2Z ":"")+y.4i,b.2w));1b d=p,c=\'<1C 2L="3S" 1w-2R="\'+b.3v+(f?", "+y.9I+" "+f:"")+(i?", "+y.9E+" "+i:"")+\'" 1c="dw-17-2k"><18 1c="dw-17-2k-3K" 1L="\'+(h?"7m:"+h+";":"")+\'"></18>\'+(j.29&&!o?\'<18 1c="dw-17-2k-2l">\'+k.1R.3Y(y.4i,j)+"</18>":"")+b.3v,e;15(b.1s&&b.2w){e=y.9K;1b g=1g.2x(b.2w-b.1s)/fq,l=g/60,s=l/60,F=s/24,r=F/cP;e=\'<18 1c="dw-17-2k-fs">\'+(45>g&&1g.2e(g)+" "+e[5].4u()||45>l&&1g.2e(l)+" "+e[4].4u()||24>s&&1g.2e(s)+" "+e[3].4u()||30>F&&1g.2e(F)+" "+e[2].4u()||cP>F&&1g.2e(F/30)+" "+e[1].4u()||1g.2e(r)+" "+e[0].4u())+"</18>"}1i e="";p=d+(c+e+"</1C>")});p+="</37>";r.22(p);g.2i("fv",[C,l]);b(C);g.2U(a(".dw-17-2k",r),16(b){J.3m||g.2i("fw",[b,e[a(1j).2z()],d])});K=!0}}16 z(){l&&l.1q("dw-17-2I-v");C&&(C.1q($).2D(".dw-i").1q(s),C.1J("1F-hl")&&C.44("1F-hl").1m(P));K=!1}16 G(a){1a 1k 1p(a.1z(),a.1o(),a.1f())}16 v(a){i={};15(a&&a.1u)1h(M=0;M<a.1u;M++)i[G(a[M])]=a[M]}16 E(){Q&&z();g.51()}1b x,o,l,C,I,J,U,r,K,M,A,T,p,B,e={};p=h({},g.3h);1b y=h(g.3h,w,p),$="dw-2q dw-17-1t-ev",P="dw-17-1t-hl",s=y.bT||"",F=y.9x||"2P"==y.bj,Y=y.fA,X=!0===y.2I||!0===y.fB,L=0,i={},Q=a.7g(y.2I),S=Q?h(!0,[],y.2I):[];p=H.cz.3a(1j,g);x=h({},p);15(y.8V)1h(M=0;M<y.8V.1u;M++)i[G(y.8V[M])]=y.8V[M];Q&&a.1n(S,16(a,b){b.4A===t&&(b.4A=L++)});g.aU=16(a,b){I=g.9t(S,a,b);A=g.9t(y.cT,a,b)};g.b8=16(b){1h(1b c=F?i[b]!==t:Q?b.29()===(1k 1p).9r(0,0,0,0):t,e=A[b]?A[b][0]:!1,f=I[b]?I[b][0]:!1,g=e||f,e=e.3v||(f?I[b].1u+" "+(1<I[b].1u?y.cN:y.cM):0),f=A[b]||I[b]||[],h=g.3K,j=X&&e?n(h):"",k="",l=\'<18 1c="dw-17-1t-m"\'+(h?\' 1L="7m-3K:\'+h+";fF-3K:"+h+" "+h+\' cU cU"\':"")+"></18>",b=0;b<f.1u;b++)f[b].8Z&&(k+=\'<1Z 1c="1v-26 1v-26-\'+f[b].8Z+\'"\'+(f[b].3v?"":f[b].3K?\' 1L="3K:\'+f[b].3K+\';"\':"")+"></1Z>\\n");15("3O"==Y){l=\'<18 1c="dw-17-1t-m"><18 1c="dw-17-1t-m-t">\';1h(b=0;b<f.1u;b++)l+=\'<18 1c="dw-17-1t-m-c"\'+(f[b].3K?\' 1L="7m:\'+f[b].3K+\';"\':"")+"></18>";l+="</18></18>"}1a{cT:g,2C:Q?!1:c,5J:Q&&c?"dw-17-1t-hl":"",bh:X||Q?e:"",1H:X&&e?\'<18 1c="dw-17-1t-4Q-c"><18 1c="dw-17-1t-4Q \'+(y.fH||"")+\'" fI="\'+a("<18>"+e+"</18>").3v()+\'"\'+(h?\' 1L="7m:\'+h+";3K:"+j+\';3v-fJ:fK;"\':"")+">"+k+e+"</18></18>":X&&k?\'<18 1c="dw-17-1t-26-c">\'+k+"</18>":g?l:""}};g.fL=16(a){i[G(a)]=a;E()};g.fM=16(a){6H i[G(a)];E()};g.7O=16(a,b,c,e,f){F&&(v(a),a=a?a[0]:25);g.3n(a,b,f,e,c);E()};g.5V=16(a){1a F?c.cg(i):g.1f(a)};g.fN=16(a,b){g.3n(a?a[0]:25,b);v(a);E()};g.fO=16(){1a F?g.5V():[g.1f()]};Q&&(g.fP=16(b){1b c=[],b=h(!0,[],a.7g(b)?b:[b]);a.1n(b,16(a,b){b.4A===t&&(b.4A=L++);S.1l(b);c.1l(b.4A)});E();1a c},g.fQ=16(b){b=a.7g(b)?b:[b];a.1n(b,16(b,d){a.1n(S,16(a,b){15(b.4A===d)1a S.a2(a,1),!1})});E()},g.fR=16(a){1b b;1a a?(a.9r(0,0,0,0),b=g.9t(S,a.1z(),a.1o()),b[a]?q(b[a]):[]):S},g.fS=16(b){1b c=[];S=h(!0,[],b);a.1n(S,16(a,b){b.4A===t&&(b.4A=L++);c.1l(b.4A)});E();1a c});h(p,{8L:!F&&!Q,8O:!F&&!Q,2v:Q&&"54"!==y.28?["4c"]:y.2v,5U:16(a){1b b,c;15(F&&a){i={};a=a.2B(",");1h(b=0;b<a.1u;b++){c=f.4q(g.2J,a[b].1D(/^\\s+|\\s+$/g,""),y);i[G(c)]=c}a=a[0]}1a x.5U.3a(1j,a)},5S:16(a){1b b,c=[];15(F){1h(b 3g i)c.1l(f.3Y(g.2J,i[b],y));1a c.ae(", ")}1a x.5S.3a(1j,a)},cJ:16(){15(F){i={};g.51()}},d5:16(){15(Q)y.2T=1e;15(y.cV)y.8O=1e;15(y.fU&&F)y.2T=16(){1b b=0,c=y.bj=="2P"?7:1;a.1n(i,16(){b++});1a b/c+" "+y.be}},91:16(b){x.91.3a(1j,b);o=b;15(F){a(".ai",b).1J("1w-4s","2y");T=h({},i)}X&&a(".dw-17",b).1m("dw-17-ev");Y&&a(".dw-17",b).1m("dw-17-m-"+Y);15(Q){b.1m("dw-17-em");l=a(\'<18 1c="dw-17-2I \'+(y.fV||"")+\'"><18 1c="dw-17-2I-5R"></18><18 1c="dw-17-2I-i"><18 1c="dw-17-2I-2h"></18></18></18>\').9h(a(".dw-17-c",b));U=a(".dw-17-2I-i",l);r=a(".dw-17-2I-2h",l);J=1k k.2O.6U(U[0]);g.2U(U,16(){J.3m||z()})}},bF:16(){Q&&z()},bK:16(){Q&&z()},9Y:16(){15(B){O(B.d,a(\'.dw-17-1t-v[1F-4l="\'+B.4l+\'"]:fW(.dw-17-1t-94)\',o));B=1e}},bI:16(b){1b c=b.2n,e=G(c),f=a(b.3x),b=b.2C;15(Q){z();f.1B("dw-17-1t-ev")||2A(16(){g.7w?B={d:e,4l:f.1J("1F-4l")}:O(e,f)},10)}1i 15(F)15(y.bj=="2P"){1b h,j,k=e.1X()-y.cK,k=k<0?7+k:k;y.9x||(i={});1h(h=0;h<7;h++){j=1k 1p(e.1z(),e.1o(),e.1f()-k+h);b?6H i[j]:i[j]=j}E()}1i{h=a(\'.dw-17 .dw-17-1t[1F-4l="\'+f.1J("1F-4l")+\'"]\',o);15(b){h.1q("dw-2q").44("1w-2C").2D(".dw-i").1q(s);6H i[e]}1i{h.1m("dw-2q").1J("1w-2C","1d").2D(".dw-i").1m(s);i[e]=e}}15(!Q&&!y.9x&&y.cV&&y.28!=="54"){g.7y=1e;g.3n(c);g.7G();1a 1e}},cf:16(){K&&b(C)},cI:16(){!g.4s&&F&&(i=h({},T))}});1a p}})(3A,5Z,5M);(16($,j){1b k=$.2d;k.1R={4f:{cW:\'+10\',3H:[\'fY\',\'fZ\',\'g0\',\'g1\',\'cX\',\'g2\',\'g3\',\'g4\',\'g5\',\'g6\',\'g7\',\'g8\'],5l:[\'g9\',\'gc\',\'gd\',\'ge\',\'cX\',\'gf\',\'gg\',\'gh\',\'gj\',\'gk\',\'gl\',\'gm\'],5o:[\'gn\',\'go\',\'gp\',\'gq\',\'gr\',\'gs\',\'gt\'],6e:[\'gu\',\'gv\',\'gw\',\'gx\',\'gy\',\'gz\',\'gA\'],cY:[\'S\',\'M\',\'T\',\'W\',\'T\',\'F\',\'S\'],83:\'av\',3T:\'am\',3E:\'gB\',1N:16(d){1a d.1z()},1o:16(d){1a d.1o()},1X:16(d){1a d.1f()},1f:16(y,m,d,h,i,s){1a 1k 1p(y,m,d,h||0,i||0,s||0)},9o:16(y,m){1a 32-1k 1p(y,m,32).1f()},bl:16(d){d=1k 1p(d);d.9r(0,0,0);d.3n(d.1f()+4-(d.1X()||7));1b a=1k 1p(d.1z(),0,1);1a 1g.9M((((d-a)/cZ)+1)/7)}},3Y:16(c,d,e){15(!d){1a 25}1b s=$.1V({},k.1R.4f,e),69=16(m){1b n=0;5a(i+1<c.1u&&c.5f(i+1)==m){n++;i++}1a n},f1=16(m,a,b){1b n=\'\'+a;15(69(m)){5a(n.1u<b){n=\'0\'+n}}1a n},f2=16(m,a,s,l){1a(69(m)?l[a]:s[a])},i,2p,2H=\'\',4Y=1e;1h(i=0;i<c.1u;i++){15(4Y){15(c.5f(i)=="\'"&&!69("\'")){4Y=1e}1i{2H+=c.5f(i)}}1i{8w(c.5f(i)){1E\'d\':2H+=f1(\'d\',s.1X(d),2);1A;1E\'D\':2H+=f2(\'D\',d.1X(),s.6e,s.5o);1A;1E\'o\':2H+=f1(\'o\',(d.29()-1k 1p(d.1z(),0,0).29())/cZ,3);1A;1E\'m\':2H+=f1(\'m\',s.1o(d)+1,2);1A;1E\'M\':2H+=f2(\'M\',s.1o(d),s.5l,s.3H);1A;1E\'y\':2p=s.1N(d);2H+=(69(\'y\')?2p:(2p%2r<10?\'0\':\'\')+2p%2r);1A;1E\'h\':1b h=d.3G();2H+=f1(\'h\',(h>12?(h-12):(h===0?12:h)),2);1A;1E\'H\':2H+=f1(\'H\',d.3G(),2);1A;1E\'i\':2H+=f1(\'i\',d.5L(),2);1A;1E\'s\':2H+=f1(\'s\',d.67(),2);1A;1E\'a\':2H+=d.3G()>11?s.3E:s.3T;1A;1E\'A\':2H+=d.3G()>11?s.3E.6j():s.3T.6j();1A;1E"\'":15(69("\'")){2H+="\'"}1i{4Y=1d}1A;d0:2H+=c.5f(i)}}}1a 2H},4q:16(c,d,e){1b s=$.1V({},k.1R.4f,e),4S=s.7P||1k 1p();15(!c||!d){1a 4S}15(d.29){1a d}d=(5x d==\'aV\'?d.gK():d+\'\');1b f=s.cW,2p=s.1N(4S),2t=s.1o(4S)+1,1t=s.1X(4S),8R=-1,4r=4S.3G(),aS=4S.5L(),aQ=0,5p=-1,4Y=1e,7p=16(a){1b b=(4x+1<c.1u&&c.5f(4x+1)==a);15(b){4x++}1a b},4v=16(a){7p(a);1b b=(a==\'@\'?14:(a==\'!\'?20:(a==\'y\'?4:(a==\'o\'?3:2)))),d4=1k 6Z(\'^\\\\d{1,\'+b+\'}\'),8s=d.8i(6G).1x(d4);15(!8s){1a 0}6G+=8s[0].1u;1a 96(8s[0],10)},7t=16(a,s,l){1b b=(7p(a)?l:s),i;1h(i=0;i<b.1u;i++){15(d.8i(6G,b[i].1u).4u()==b[i].4u()){6G+=b[i].1u;1a i+1}}1a 0},8a=16(){6G++},6G=0,4x;1h(4x=0;4x<c.1u;4x++){15(4Y){15(c.5f(4x)=="\'"&&!7p("\'")){4Y=1e}1i{8a()}}1i{8w(c.5f(4x)){1E\'d\':1t=4v(\'d\');1A;1E\'D\':7t(\'D\',s.6e,s.5o);1A;1E\'o\':8R=4v(\'o\');1A;1E\'m\':2t=4v(\'m\');1A;1E\'M\':2t=7t(\'M\',s.5l,s.3H);1A;1E\'y\':2p=4v(\'y\');1A;1E\'H\':4r=4v(\'H\');1A;1E\'h\':4r=4v(\'h\');1A;1E\'i\':aS=4v(\'i\');1A;1E\'s\':aQ=4v(\'s\');1A;1E\'a\':5p=7t(\'a\',[s.3T,s.3E],[s.3T,s.3E])-1;1A;1E\'A\':5p=7t(\'A\',[s.3T,s.3E],[s.3T,s.3E])-1;1A;1E"\'":15(7p("\'")){8a()}1i{4Y=1d}1A;d0:8a()}}}15(2p<2r){2p+=1k 1p().1z()-1k 1p().1z()%2r+(2p<=(5x f!=\'9T\'?f:1k 1p().1z()%2r+96(f,10))?0:-2r)}15(8R>-1){2t=1;1t=8R;do{1b g=32-1k 1p(2p,2t-1,32).1f();15(1t<=g){1A}2t++;1t-=g}5a(1d)}4r=(5p==-1)?4r:((5p&&4r<12)?(4r+12):(!5p&&4r==12?0:4r));1b h=s.1f(2p,2t-1,1t,4r,aS,aQ);15(s.1N(h)!=2p||s.1o(h)+1!=2t||s.1X(h)!=1t){1a 4S}1a h}};k.3Y=k.1R.3Y;k.4q=k.1R.4q})(3A);(16($,z){1b A=$.2d,1R=A.1R,2n=1k 1p(),4f={6P:2n.1z()-2r,6O:2n.1z()+1,92:\' \',63:\'3z/dd/2Z\',47:\'gX\',6J:\'ci\',4i:\'hh:43 A\',6N:\'cl\',7R:\'90\',6Q:\'cm\',6R:\'cn\',8E:\'&aY;\',6V:\'cp\',6Y:\'cq\'},2S=16(l){1b n=$(1j),7x={},2J;15(n.6l(\'77\')){8w(n.1J(\'2g\')){1E\'2n\':2J=\'2Z-3z-dd\';1A;1E\'1R\':2J=\'2Z-3z-8t:43:cs\';1A;1E\'1R-ct\':2J=\'2Z-3z-8t:43:3W\';1A;1E\'2t\':2J=\'2Z-3z\';7x.47=\'cu\';1A;1E\'2l\':2J=\'b2:43:3W\';1A}1b q=n.1J(\'2c\'),1G=n.1J(\'1G\');15(q){7x.4V=1R.4q(2J,q)}15(1G){7x.4U=1R.4q(2J,1G)}}1b i,k,1Q,21,3M,1s,2w,9O,5G,46,dc=$.1V({},l.3h),s=$.1V(l.3h,A.1R.4f,4f,7x,dc),2s=0,5j=[],2Y=[],4Z=[],o={},f={y:1N,m:1o,d:1X,h:df,i:dg,s:di,a:dj},4N=s.4N,3f=s.3f,p=s.2S,5D=s.47,5n=s.6J,dl=5D.1x(/D/),5p=5n.1x(/a/i),5C=5n.1x(/h/),8N=p==\'1R\'?s.63+s.92+s.4i:p==\'2l\'?s.4i:s.63,dm=1k 1p(),3l=s.3l||{},6m=3l.cw||s.cx||1,5z=3l.cy||s.bk||1,7F=3l.cA||s.cB||1,6v=3l.6v,3k=s.4V||1k 1p(s.6P,0,1),4d=s.4U||1k 1p(s.6O,11,31,23,59,59),7M=6v?0:3k.3G()%6m,6E=6v?0:3k.5L()%5z,7Z=6v?0:3k.67()%7F,9V=7S(6m,7M,(5C?11:23)),9F=7S(5z,6E,59),9D=7S(5z,6E,59);2J=2J||8N;15(p.1x(/2n/i)){$.1n([\'y\',\'m\',\'d\'],16(j,v){i=5D.6D(1k 6Z(v,\'i\'));15(i>-1){4Z.1l({o:i,v:v})}});4Z.72(16(a,b){1a a.o>b.o?1:-1});$.1n(4Z,16(i,v){o[v.v]=i});3M=[];1h(k=0;k<3;k++){15(k==o.y){2s++;21=[];1Q=[];1s=s.1N(3k);2w=s.1N(4d);1h(i=1s;i<=2w;i++){1Q.1l(i);21.1l((5D.1x(/2Z/i)?i:(i+\'\').8i(2,2))+(s.aG||\'\'))}58(3M,1Q,21,s.7R)}1i 15(k==o.m){2s++;21=[];1Q=[];1h(i=0;i<12;i++){1b r=5D.1D(/[dy]/gi,\'\').1D(/3z/,(i<9?\'0\'+(i+1):i+1)+(s.8f||\'\')).1D(/m/,i+1+(s.8f||\'\'));1Q.1l(i);21.1l(r.1x(/6n/)?r.1D(/6n/,\'<1Z 1c="dw-84">\'+s.3H[i]+\'</1Z>\'):r.1D(/M/,\'<1Z 1c="dw-84">\'+s.5l[i]+\'</1Z>\'))}58(3M,1Q,21,s.83)}1i 15(k==o.d){2s++;21=[];1Q=[];1h(i=1;i<32;i++){1Q.1l(i);21.1l((5D.1x(/dd/i)&&i<10?\'0\'+i:i)+(s.6k||\'\'))}58(3M,1Q,21,s.6N)}}2Y.1l(3M)}15(p.1x(/2l/i)){9O=1d;4Z=[];$.1n([\'h\',\'i\',\'s\',\'a\'],16(i,v){i=5n.6D(1k 6Z(v,\'i\'));15(i>-1){4Z.1l({o:i,v:v})}});4Z.72(16(a,b){1a a.o>b.o?1:-1});$.1n(4Z,16(i,v){o[v.v]=2s+i});3M=[];1h(k=2s;k<2s+4;k++){15(k==o.h){2s++;21=[];1Q=[];1h(i=7M;i<(5C?12:24);i+=6m){1Q.1l(i);21.1l(5C&&i===0?12:5n.1x(/hh/i)&&i<10?\'0\'+i:i)}58(3M,1Q,21,s.6Q)}1i 15(k==o.i){2s++;21=[];1Q=[];1h(i=6E;i<60;i+=5z){1Q.1l(i);21.1l(5n.1x(/43/)&&i<10?\'0\'+i:i)}58(3M,1Q,21,s.6R)}1i 15(k==o.s){2s++;21=[];1Q=[];1h(i=7Z;i<60;i+=7F){1Q.1l(i);21.1l(5n.1x(/3W/)&&i<10?\'0\'+i:i)}58(3M,1Q,21,s.6V)}1i 15(k==o.a){2s++;1b u=5n.1x(/A/);58(3M,[0,1],u?[s.3T.6j(),s.3E.6j()]:[s.3T,s.3E],s.8E)}}2Y.1l(3M)}16 3w(d,i,a){15(o[i]!==z){1a+d[o[i]]}15(a!==z){1a a}1a f[i](dm)}16 58(a,k,v,b){a.1l({21:v,1Q:k,2R:b})}16 3V(v,a,b,c){1a 1g.2c(c,1g.4y(v/a)*a+b)}16 1N(d){1a s.1N(d)}16 1o(d){1a s.1o(d)}16 1X(d){1a s.1X(d)}16 df(d){1b a=d.3G();a=5C&&a>=12?a-12:a;1a 3V(a,6m,7M,9V)}16 dg(d){1a 3V(d.5L(),5z,6E,9F)}16 di(d){1a 3V(d.67(),7F,7Z,9D)}16 dj(d){1a 5p&&d.3G()>11?1:0}16 1f(d){15(d===25){1a d}1b a=3w(d,\'h\',0);1a s.1f(3w(d,\'y\'),3w(d,\'m\'),3w(d,\'d\',1),3w(d,\'a\',0)?a+12:a,3w(d,\'i\',0),3w(d,\'s\',0))}16 7S(a,b,c){1a 1g.4y((c-b)/a)*a+b}16 dx(d,a){1b b,1Y,7N=1e,6w=1e,aT=0,aJ=0;15(6p(d)){1a d}15(d<3k){d=3k}15(d>4d){d=4d}b=d;1Y=d;15(a!==2){7N=6p(b);5a(!7N&&b<4d){b=1k 1p(b.29()+ad*60*60*24);7N=6p(b);aT++}}15(a!==1){6w=6p(1Y);5a(!6w&&1Y>3k){1Y=1k 1p(1Y.29()-ad*60*60*24);6w=6p(1Y);aJ++}}15(a===1&&7N){1a b}15(a===2&&6w){1a 1Y}1a aJ<aT&&6w?1Y:b}16 6p(d){15(d<3k){1a 1e}15(d>4d){1a 1e}15(ac(d,3f)){1a 1d}15(ac(d,4N)){1a 1e}1a 1d}16 ac(d,a){1b b,j,v;15(a){1h(j=0;j<a.1u;j++){b=a[j];v=b+\'\';15(!b.1s){15(b.29){15(d.1z()==b.1z()&&d.1o()==b.1o()&&d.1f()==b.1f()){1a 1d}}1i 15(!v.1x(/w/i)){v=v.2B(\'/\');15(v[1]){15((v[0]-1)==d.1o()&&v[1]==d.1f()){1a 1d}}1i 15(v[0]==d.1f()){1a 1d}}1i{v=+v.1D(\'w\',\'\');15(v==d.1X()){1a 1d}}}}}1a 1e}16 ab(a,y,m,b,c,e,f){1b j,d,v;15(a){1h(j=0;j<a.1u;j++){d=a[j];v=d+\'\';15(!d.1s){15(d.29){15(s.1N(d)==y&&s.1o(d)==m){e[s.1X(d)-1]=f}}1i 15(!v.1x(/w/i)){v=v.2B(\'/\');15(v[1]){15(v[0]-1==m){e[v[1]-1]=f}}1i{e[v[0]-1]=f}}1i{v=+v.1D(\'w\',\'\');1h(k=v-b;k<c;k+=7){15(k>=0){e[k]=f}}}}}}}16 a6(b,i,v,c,y,m,d,e,f){1b g,3W,r,35,2X,5w,5v,5u,5t,j,i1,i2,9e,6d,6b,9d,8K,7Q,aP={},3l={h:6m,i:5z,s:7F,a:1},1t=s.1f(y,m,d),w=[\'a\',\'h\',\'i\',\'s\'];15(b){$.1n(b,16(i,a){15(a.1s){a.4F=1e;g=a.d;3W=g+\'\';r=3W.2B(\'/\');15(g&&((g.29&&y==s.1N(g)&&m==s.1o(g)&&d==s.1X(g))||(!3W.1x(/w/i)&&((r[1]&&d==r[1]&&m==r[0]-1)||(!r[1]&&d==r[0])))||(3W.1x(/w/i)&&1t.1X()==+3W.1D(\'w\',\'\')))){a.4F=1d;aP[1t]=1d}}});$.1n(b,16(x,a){9e=0;6d=0;i1=0;i2=z;5w=1d;5v=1d;6b=1e;15(a.1s&&(a.4F||(!a.d&&!aP[1t]))){35=a.1s.2B(\':\');2X=a.2w.2B(\':\');1h(j=0;j<3;j++){15(35[j]===z){35[j]=0}15(2X[j]===z){2X[j]=59}35[j]=+35[j];2X[j]=+2X[j]}35.7q(35[0]>11?1:0);2X.7q(2X[0]>11?1:0);15(5C){15(35[1]>=12){35[1]=35[1]-12}15(2X[1]>=12){2X[1]=2X[1]-12}}1h(j=0;j<i;j++){15(5j[j]!==z){5u=3V(35[j],3l[w[j]],5G[w[j]],46[w[j]]);5t=3V(2X[j],3l[w[j]],5G[w[j]],46[w[j]]);9d=0;8K=0;7Q=0;15(5C&&j==1){9d=35[0]?12:0;8K=2X[0]?12:0;7Q=5j[0]?12:0}15(!5w){5u=0}15(!5v){5t=46[w[j]]}15((5w||5v)&&(5u+9d<5j[j]+7Q&&5j[j]+7Q<5t+8K)){6b=1d}15(5j[j]!=5u){5w=1e}15(5j[j]!=5t){5v=1e}}}15(!f){1h(j=i+1;j<4;j++){15(35[j]>0){9e=3l[v]}15(2X[j]<46[w[j]]){6d=3l[v]}}}15(!6b){5u=3V(35[i],3l[v],5G[v],46[v])+9e;5t=3V(2X[i],3l[v],5G[v],46[v])-6d;15(5w){i1=9G(e,5u,46[v],0)}15(5v){i2=9G(e,5t,46[v],1)}}15(5w||5v||6b){15(f){$(\'.dw-1C\',e).3p(i1,i2).1m(\'dw-v\')}1i{$(\'.dw-1C\',e).3p(i1,i2).1q(\'dw-v\')}}}})}}16 8F(t,v){1a $(\'.dw-1C\',t).2z($(\'.dw-1C[1F-1M="\'+v+\'"]\',t))}16 9G(t,v,a,b){15(v<0){1a 0}15(v>a){1a $(\'.dw-1C\',t).1u}1a 8F(t,v)+b}16 65(d){1b i,3Z=[];15(d===25||d===z){1a d}1h(i 3g o){3Z[o[i]]=f[i](d)}1a 3Z}16 aX(a){1b i,v,1s,3Z=[];15(a){1h(i=0;i<a.1u;i++){v=a[i];15(v.1s&&v.1s.29){1s=1k 1p(v.1s);5a(1s<=v.2w){3Z.1l(1k 1p(1s.1z(),1s.1o(),1s.1f()));1s.3n(1s.1f()+1)}}1i{3Z.1l(v)}}1a 3Z}1a a}l.5V=16(a){1a l.3t||a?1f(l.4T(a)):25};l.3n=16(d,a,b,c,e){l.b7(65(d),a,e,c,b)};l.1f=l.5V;l.2J=8N;l.95=o;l.8g.6g=16(){l.3n(1k 1p(),1e,0.3,1d,1d)};l.2v.6g={3v:s.6Y,3u:\'6g\'};4N=aX(4N);3f=aX(3f);3k=1f(65(3k));4d=1f(65(4d));5G={y:3k.1z(),m:0,d:1,h:7M,i:6E,s:7Z,a:0};46={y:4d.1z(),m:11,d:31,h:9V,i:9F,s:9D,a:1};1a{2Y:2Y,2T:s.2T?16(){1a 1R.3Y(8N,1f(l.4T(1d)),s)}:1e,5S:16(d){1a 1R.3Y(2J,1f(d),s)},5U:16(a){1a 65(a?1R.4q(2J,a,s):(s.7P||1k 1p()))},aB:16(g,i,h,j){1b k=dx(1f(l.4T(1d)),j),4M=65(k),y=3w(4M,\'y\'),m=3w(4M,\'m\'),8J=1d,8G=1d;$.1n([\'y\',\'m\',\'d\',\'a\',\'h\',\'i\',\'s\'],16(x,i){15(o[i]!==z){1b b=5G[i],1G=46[i],6c=31,1M=3w(4M,i),t=$(\'.dw-37\',g).eq(o[i]);15(i==\'d\'){6c=s.9o(y,m);1G=6c;15(dl){$(\'.dw-1C\',t).1n(16(){1b a=$(1j),d=a.1F(\'1M\'),w=s.1f(y,m,d).1X(),r=5D.1D(/[cH]/gi,\'\').1D(/dd/,(d<10?\'0\'+d:d)+(s.6k||\'\')).1D(/d/,d+(s.6k||\'\'));$(\'.dw-i\',a).22(r.1x(/9p/)?r.1D(/9p/,\'<1Z 1c="dw-1t">\'+s.5o[w]+\'</1Z>\'):r.1D(/D/,\'<1Z 1c="dw-1t">\'+s.6e[w]+\'</1Z>\'))})}}15(8J&&3k){b=f[i](3k)}15(8G&&4d){1G=f[i](4d)}15(i!=\'y\'){1b c=8F(t,b),i2=8F(t,1G);$(\'.dw-1C\',t).1q(\'dw-v\').3p(c,i2+1).1m(\'dw-v\');15(i==\'d\'){$(\'.dw-1C\',t).1q(\'dw-h\').3p(6c).1m(\'dw-h\')}}15(1M<b){1M=b}15(1M>1G){1M=1G}15(8J){8J=1M==b}15(8G){8G=1M==1G}15(i==\'d\'){1b e=s.1f(y,m,1).1X(),4W={};ab(4N,y,m,e,6c,4W,1);ab(3f,y,m,e,6c,4W,0);$.1n(4W,16(i,v){15(v){$(\'.dw-1C\',t).eq(i).1q(\'dw-v\')}})}}});15(9O){$.1n([\'a\',\'h\',\'i\',\'s\'],16(i,v){1b a=3w(4M,v),d=3w(4M,\'d\'),t=$(\'.dw-37\',g).eq(o[v]);15(o[v]!==z){a6(4N,i,v,4M,y,m,d,t,0);a6(3f,i,v,4M,y,m,d,t,1);5j[i]=+l.9z(a,t,j).1M}})}l.3X=4M}}};$.1n([\'2n\',\'2l\',\'1R\'],16(i,v){A.6C.4J[v]=2S})})(3A);(16($){$.1n([\'2n\',\'2l\',\'1R\'],16(i,v){$.2d.8X(v)})})(3A);(16($){$.2d.8T.dO=$.1V($.2d.8T.dO,{ax:\'确定\',aC:\'取消\',aE:\'明确\',be:\'选\',63:\'2Z-3z-dd\',47:\'hY\',5o:[\'周日\',\'周一\',\'周二\',\'周三\',\'周四\',\'周五\',\'周六\'],6e:[\'日\',\'一\',\'二\',\'三\',\'四\',\'五\',\'六\'],cY:[\'日\',\'一\',\'二\',\'三\',\'四\',\'五\',\'六\'],6N:\'日\',6Q:\'时\',6R:\'分\',5l:[\'1月\',\'2月\',\'3月\',\'4月\',\'5月\',\'6月\',\'7月\',\'8月\',\'9月\',\'10月\',\'11月\',\'12月\'],3H:[\'一\',\'二\',\'三\',\'四\',\'五\',\'六\',\'七\',\'八\',\'九\',\'十\',\'十一\',\'十二\'],83:\'月\',6V:\'秒\',4i:\'b2:43\',6J:\'hZ\',7R:\'年\',6Y:\'现在\',3E:\'下午\',3T:\'上午\',bu:\'日\',bt:\'时间\',an:\'日历\',aA:\'关闭\',9I:\'开始时间\',9E:\'结束时间\',i0:\'合计\',i3:\'分数\',i4:\'单位\',7d:[\'年\',\'月\',\'日\',\'小时\',\'分钟\',\'秒\',\'\'],9K:[\'年\',\'月\',\'日\',\'点\',\'分\',\'秒\',\'\'],i5:\'开始\',i6:\'停止\',i7:\'重置\',i8:\'圈\',e0:\'隐藏\'})})(3A);',62,1127,'|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||if|function|cal|div|that|return|var|class|true|false|getDate|Math|for|else|this|new|push|addClass|each|getMonth|Date|removeClass|on|start|day|length|mbsc|aria|match|dwb|getFullYear|break|hasClass|li|replace|case|data|max|markup|btn|attr|ma|style|val|getYear|px|height|keys|datetime|ka|sa|pa|extend|ms|getDay|prev|span||values|html|||null|ic||display|getTime|width|top|min|mobiscroll|round|va|type|sc|trigger|calendar|event|time|util|date|na|year|sel|100|offset|month|itemHeight|buttons|end|abs|off|index|setTimeout|split|selected|find|next|preventDefault|elm|output|events|format|ra|role|css|scroll|classes|week|ta|label|preset|headerText|tap|mousedown|theme|parts2|wheels|yy||||dwwl||parts1||ul||mouseup|call|Ga|has3d|click|qa|valid|in|settings|Ia|instances|mind|steps|scrolled|setDate|nh|slice|constrain|isModal|Ca|_hasValue|handler|text|get|cell|ja|mm|jQuery|la|getCoord|target|pmText|left|getHours|monthNames|Ra|nw|color|tabindex|wg|Pa|bottom|id|snap|wnd|button|amText|disabled|step|ss|_tempWheelArray|formatDate|ret||empty||ii|removeAttr||maxs|dateOrder|closest|doAnim|Ha|focus|cancel|maxd|moved|defaults|pnl|touchstart|timeFormat|popup|arrow|full|touchend|_tempValue|oa|200|parseDate|hours|live|stop|toLowerCase|getNumber|dist|iFormat|floor|set|_id|hasFlex|init|outerHeight|nr|apply|st|ua|Da|scroller|La|xa|temp|invalid|scrollLock|modalHeight|txt|bubble|def|getArrayVal|maxDate|minDate|idx|ctx|literal|ord||refresh|pr|matrix|inline||_isVisible|slide|addWheel||while|dist1|Aa|onBtnEnd|Qa|charAt|lang|setValue|Ta|validValues|300|monthNamesShort|isString|tord|dayNames|ampm|controls|testTouch|persp|v2|v1|prop2|prop1|typeof|test|stepM|userdef|hidden|hampm|dord|context|iv|mins|tapped|Za|cssClass|stopPropagation|getMinutes|document|value|anchor|dist2|hide|arr|formatResult|arrw|parseValue|getVal|outerWidth|Ja|ya|window||liquid|touchmove|dateFormat|keydown|getArray|lb|getSeconds|_isInput|look|toDateString|all|maxdays|remove|dayNamesShort|clear|now|prefix|show|toUpperCase|daySuffix|is|stepH|MM|out|isValid|isScrollable|row|rtl|sl|innerWidth|zeroBased|prevValid|lines|themes|modalWidth|pos|rows|presets|search|minM|keyCode|iValue|delete|onShow|timeWheels|jsPrefix|anim|table|dayText|endYear|startYear|hourText|minuteText|layout|dwc|ScrollView|secText|_isFullScreen|tabs|nowText|RegExp|minw|totalw|sort|active|multiple|scrollToPos|cell2|input|cells|_value|position|onPosition|za|labels|Xa|Ea|isArray|onHide|ariaMessage|minWidth|contSize|maxScroll|background|calc|prototype|lookAhead|unshift|mousemove|firstDay|getName|setGlobals|nb|changing|html5def|needsSlide|timer|destroy|body|btnCalNextClass|btnCalPrevClass|preventShow|stepS|select|pb|Transform|setReadOnly|sb|posEvents|minH|nextValid|setVal|defaultValue|hours3|yearText|getMax|trans|showLabel|_isLiquid|getComputedStyle|wb|elastic|minS||Transition|weekDays|monthText|mon|translate3d|isReadOnly|mode|testProps|animationend|checkLiteral|ios7|btnWidth|ob|res|monthSuffix|handlers|clearTimeout|substr|dwa|Na|Bb|onMove|timeUnit|onEnd|tb|_wheelArray|speed|num|ddTHH|minScroll|tindex|switch|mb|Fa|inst|modal|speedUnit|touchcancel|_readValue|ampmText|getIndex|maxprop|prop|maxWidth|minprop|hours2|highlight|tbl|hformat|divergentDayChange|yb|needsRefresh|doy|tr|i18n|_defaults|selectedValues|dwwb|presetShort|liq|icon|Year|onMarkupReady|separator|Wa|diff|order|parseInt|control|nhl|onBtnStart|wasReadOnly|tab|preventPos|hours1|add|btnw|readonly|appendTo|hasButtons|animEnd|wrapper|Moz|Scroller|webkitAnimationEnd|getMaxDayOfMonth|DD|xb|setHours|line|prepareObj|header|Widget|isOldAndroid|multiSelect|maxSnapScroll|getValidCell|dwtd|Ib|breakpoint|maxS|toText|maxM|getValidIndex|generateWheelItems|fromText|readOnly|labelsShort|pixels|ceil|isVisible|hasTime|touch|fixedWidth|selectedLineHeight|selectedLineBorder|string|dwwms|maxH|_processSettings|dwsw|onMonthLoaded|wa|curr|ready|splice|parentClass|isNumeric|nrs|validateTimes|months|btnc|startY||validateDates|isInObj|1000|join|getValid|btnPlusClass||dwv|page|_attachEvents|||calendarText|rb||getPosition|navigate|btnMinusClass||_fillValue|Month||setText|Ba|Va|closeText|validate|cancelText|showOnTap|clearText|axis|yearSuffix|showOnFocus|onWndKeyDown|down|posDebounce|elmList|wndHeight|wndWidth|_preventChange|spec|seconds|_generateContent|minutes|up|onGenMonth|object|calendarClass|convertRanges|nbsp|arrl|plus|components|HH|minus|overlay|clearInterval|startTime|setArrayVal|getDayProps|zb|||kb|_markupReady|selectedText||ub|ariaLabel|prevdef|selectType|stepMinute|getWeekNumber|nextYearText|prevYearText|Next|nextMonthText|Previous|prevMonthText|todayText|timeText|dateText|yearMonth|navigation|quickNav|dir|slideNr|load|callback|liveSwipe|swipe|preMonths|onMonthChange|maxMonthWidth|addEventListener|onDayChange|ransform|onSelectShow|focusOnClose|snap1|snap2|Sa|ib|activeElement|innerHeight|momentum|activeClass|scrollTop|cancelAnimationFrame|requestAnimationFrame|ios|changedTouches|originalEvent|auto|280|showDivergentDays|days|th|multiline|keyup|_dw_pnl_|append|margin|||dwbtn|calendarHeight|tabbed|onCalResize|objectToArray|td|hhiiA|cellspacing|cellpadding|Day|Hours|Minutes|inArray|Seconds|Now|864E5|ssZ|local|mmyy|arguments|hour|stepHour|minute|calbase|second|stepSecond|onValidated|attachShow|ease|transform|formatHeader|my|onCancel|onClear|firstSelectDay|delay|eventText|eventsText|detail|365|wheelDelta|onScroll|onKeyUp|marked|transparent|closeOnSelect|shortYearCutoff|May|dayNamesMin|86400000|default|onKeyDown|dwwbp|ttop|digits|onBeforeShow|baseTheme|getCurrentPosition|onStart|dwo|||orig||component|getHour|getMinute||getSecond|getAmPm|dwwr|regen|defd|right5||left5|up5|down5|checkmark|ion|checkIcon|dwcc||getClosestValidDate||change|_class|onThemeLoad|testPrefix|mod|getInst|option|_isValid|btnClass|closeOnOverlay|_markup|_header|activeInstance|DOMMouseScroll|mousewheel|zh|ndash|dwwbm|arrayToObject|moveElement|dwfl|mmddy|setDefaults|Today|msT|offsetWidth|600px|hideText|dwwc|dwhl|OT|dwsc|offsetHeight|dwpm|offsetLeft|offsetTop|changeWheel|||onBtnTap|Calendar|getValue|Time|_getVal|inv|MozT|version|try|_setVal|||catch|onValueFill||menustrip|listview|Min|Short||weekCounter||white|space|nowrap|onChange|webkitT|scrollHeight|userAgent|textarea|onSelect|calendarWidth|toFixed|vertical|moz|swipeDirection|size|WebkitBoxDirection|6048E5|msFlex|blur|innerDayClass|multiInst|scrollLeft|enable|Yrs|Mths|Days|Hrs|Mins|Secs|||onDayHighlight|Start|numpad|End|0022|activeTabClass|flex|||setInterval|msPerspective|backgroundColor|rgb||rgba|130|299|587|114|000||fff|list|1E3|activeTabInnerClass|dur|disabledClass|frame|onEventBubbleShow|onEventSelect|120|right6|pageshow|markedDisplay|markedText|navigator|OPerspective|backspace3|border|deleteIcon|eventTextClass|title|shadow|none|addValue|removeValue|setValues|getValues|addEvent|removeEvent|getEvents|setEvents|useShortLabels|counter|eventBubbleClass|not|parent|January|February|March|April|June|July|August|September|October|November|December|Jan|||Feb|Mar|Apr|Jun|Jul|Aug||Sep|Oct|Nov|Dec|Sunday|Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sun|Mon|Tue|Wed|Thu|Fri|Sat|pm|left6|MMdyy|MozPerspective|WebkitPerspective|perspectiveProperty|modernizr|400|createElement|toString|08|android|onValueTap|disable|slidedown|slideup|Array|no3d|0012|shift|001|right|mmddyy|old|clickpick|nobtn|_selectedValues|parseFloat|dialog|multi|weeks|ml|ym|ltr|today|||dayClass|assertive|mouseover|170|first||weekNrClass|dwwol|short||tablist|dwwo|MMddyy|transitionend|Clear|Cancel|Close|Selected|Webkit|href|Set|onInit|Text|animate|before|webkitTransitionEnd|dwbc|onTabChange|scrollview|dww|onDestroy|500|dwbw|onClose|dwww|listbox|1024|640|last|selectstart|onMarkupInserted|insertAfter|orientationchange|resize|children|dwl|mdy|onSetDate|yymmdd|HHii|wholeText|||fractionText|unitText|startText|stopText|resetText|lapText|font|'.split('|'),0,{})); 
/**
添加$.version以解决jQuery1.9后不支持version问题
*/
(function(jQuery){  
if(jQuery.browser) return;  
jQuery.browser = {};  
jQuery.browser.mozilla = false;  
jQuery.browser.webkit = false;  
jQuery.browser.opera = false;  
jQuery.browser.msie = false;  
var nAgt = navigator.userAgent;  
jQuery.browser.name = navigator.appName;  
jQuery.browser.fullVersion = ''+parseFloat(navigator.appVersion);  
jQuery.browser.majorVersion = parseInt(navigator.appVersion,10);  
var nameOffset,verOffset,ix;  
// In Opera, the true version is after "Opera" or after "Version"  
if ((verOffset=nAgt.indexOf("Opera"))!=-1) {  
jQuery.browser.opera = true;  
jQuery.browser.name = "Opera";  
jQuery.browser.fullVersion = nAgt.substring(verOffset+6);  
if ((verOffset=nAgt.indexOf("Version"))!=-1)  
jQuery.browser.fullVersion = nAgt.substring(verOffset+8);  
}  
// In MSIE, the true version is after "MSIE" in userAgent  
else if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {  
jQuery.browser.msie = true;  
jQuery.browser.name = "Microsoft Internet Explorer";  
jQuery.browser.fullVersion = nAgt.substring(verOffset+5);  
}  
// In Chrome, the true version is after "Chrome"  
else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {  
jQuery.browser.webkit = true;  
jQuery.browser.name = "Chrome";  
jQuery.browser.fullVersion = nAgt.substring(verOffset+7);  
}  
// In Safari, the true version is after "Safari" or after "Version"  
else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {  
jQuery.browser.webkit = true;  
jQuery.browser.name = "Safari";  
jQuery.browser.fullVersion = nAgt.substring(verOffset+7);  
if ((verOffset=nAgt.indexOf("Version"))!=-1)  
jQuery.browser.fullVersion = nAgt.substring(verOffset+8);  
}  
// In Firefox, the true version is after "Firefox"  
else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {  
jQuery.browser.mozilla = true;  
jQuery.browser.name = "Firefox";  
jQuery.browser.fullVersion = nAgt.substring(verOffset+8);  
}  
// In most other browsers, "name/version" is at the end of userAgent  
else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) <  
(verOffset=nAgt.lastIndexOf('/')) )  
{  
jQuery.browser.name = nAgt.substring(nameOffset,verOffset);  
jQuery.browser.fullVersion = nAgt.substring(verOffset+1);  
if (jQuery.browser.name.toLowerCase()==jQuery.browser.name.toUpperCase()) {  
jQuery.browser.name = navigator.appName;  
}  
}  
// trim the fullVersion string at semicolon/space if present  
if ((ix=jQuery.browser.fullVersion.indexOf(";"))!=-1)  
jQuery.browser.fullVersion=jQuery.browser.fullVersion.substring(0,ix);  
if ((ix=jQuery.browser.fullVersion.indexOf(" "))!=-1)  
jQuery.browser.fullVersion=jQuery.browser.fullVersion.substring(0,ix);  
jQuery.browser.majorVersion = parseInt(''+jQuery.browser.fullVersion,10);  
if (isNaN(jQuery.browser.majorVersion)) {  
jQuery.browser.fullVersion = ''+parseFloat(navigator.appVersion);  
jQuery.browser.majorVersion = parseInt(navigator.appVersion,10);  
}  
jQuery.browser.version = jQuery.browser.majorVersion;  
})(jQuery);  


var PermissionType_READONLY = 1;
var PermissionType_MODIFY = 2;
var PermissionType_HIDDEN = 3;
var PermissionType_DISABLED = 4;
var PermissionType_PRINT = 5;
/**
 * 表单控件重构时调用
 * 通过id获取非后台显式添加（后台开发时在表单源码添加）的其他属性
 * objId:moduleOtherAttrs属性值
 * return:所有元素属性组成的html
 **/
function getOtherProps(objId) {
	var el = jQuery("[moduleOtherAttrs='"+objId+"']")[0], atts = el.attributes, att, html="";
	for (var i=0; i < atts.length; i++) {
		att = atts[i];
		if (att.specified 
				&& att.name.toLowerCase() != "moduleotherattrs" 
				&& att.name != "type" && att.value != "" 
				&& att.name.toLowerCase().indexOf("jquery") == -1) {
			html += " " +att.name+ "='" +att.value+ "'";
		}
	}
	jQuery(el).remove("input");
	return html;
}

/**
 * 表单控件重构时调用
 * 通过对象获取非后台显式添加（后台开发时在表单源码添加）的其他属性
 * obj:获取属性的对象
 * @return:所有元素属性组成的html
 */
function getOtherAttrs(obj) {
	var atts = obj.attributes, att, html="";
	for (var i=0; i < atts.length; i++) {
		att = atts[i];
		if (att.specified 
				&& att.name.toLowerCase() != "moduletype" 
				&& att.name.toLowerCase() != "classname" 
				&& att.name.toLowerCase() != "discript" 
				&& att.name.toLowerCase() != "style" 
				&& att.name.toLowerCase() != "class" 
				&& att.name.toLowerCase() != "value"
				&& att.name != "type" && att.value != "" 
				&& att.name.substr(0,1) != "_"
				&& att.name.toLowerCase().indexOf("jquery") == -1) {
			html += " " +att.name+ "='" +att.value+ "'";
		}
	}
	return html;
}

//当表单配置有待办控件时，把待办load进页面
function loadPedding(){
	jQuery(".ElementDiv").each(
			function(index){
				jQuery(this).load(jQuery(this).attr("src"),{'index':index},function(){
					jQuery("[moduleType='pending']").obpmPending();  //待办元素
				});
			}
		);
}

//--表单控件jquery重构--start--//
/**
 * jquery重构按钮和控件
 * for:表单
 */
function jqRefactor(){
	try{
		var $actButtion = jQuery("input[moduleType='activityButton']");
		if($actButtion.size() != 0)
			$actButtion.obpmButton(); //操作按钮

		jQuery("input[moduleType='IncludedView']").filter(function(){
			return (jQuery(this).parents("div[moduleType='TabNormal']").size() == 0);
		}).obpmIncludedView();  		//包含元素
		
		//必须放在选项卡重构前
		// jQuery("div[moduleType='TabNormal']").filter(function(){
		// 	return (jQuery(this).parents("div[moduleType='TabNormal']").size() == 0);
		// }).obpmTabNormalField();  		//页签选项卡

		//必须放在选项卡重构前
		jQuery("div[moduleType='TabNormal']").filter(function(){
			return (jQuery(this).parents("div[moduleType='TabNormal']").size() == 0);
		}).obpmTabNormalField();  
		
		jQuery("ul[moduleType='TabCollapse']").filter(function(){
			return (jQuery(this).parents("ul[moduleType='TabCollapse']").size() == 0);
		}).obpmTabCollapseField();  		//页签选项卡
		/****/
		//以下控件必须在选项卡后重构
		jQuery("input[moduleType='formActivityButton']").obpmButtonField();		//下拉选择框
		jQuery("span[moduleType='select']").obpmSelectField();		//下拉选择框
		jQuery("input[moduleType='input']").obpmInputField();		//单行文本框
		jQuery("textarea[moduleType='textarea']").obpmTextareaField();	//多行文本框
		jQuery("span[moduleType='radio']").obpmRadioField();		//单选框
		jQuery("span[moduleType='checkbox']").obpmCheckbox();		//复选框
		jQuery("select[moduleType='department']").obpmDepartmentField();			//部门选择框
		jQuery("input[moduleType='viewDialog']").obpmViewDialog();				//视图选择框
		jQuery("input[moduleType='treeDepartment']").obpmTreeDepartmentField();	//树形部门选择框
		jQuery("input[moduleType='takePhoto']").obpmTakePhoto();					//在线拍照
		jQuery("select[moduleType='selectAbout']").obpmSelectAboutField();  		//左右选择框
		//setTimeout(function(){
			jQuery("input[moduleType='dateinput']").obpmDateField();		//日期控件
			jQuery("input[moduleType='userfield']").obpmUserfield();  				//用户选择框
			jQuery("input[moduleType='suggest']").obpmSuggestField();  				//下拉提示框
			jQuery("span[moduleType='uploadFile']").obpmUploadField();  			//文件（图片）上传功能
//			jQuery("input[moduleType='uploadToDatabase']").obpmAttachmentUploadToDataBase();  //文件上传到数据库
//			jQuery("input[moduleType='ImageUploadToDatabase']").obpmImageUploadToDataBase();  //图片上传到数据库
			jQuery("textarea[moduleType='htmlEditor']").obpmHtmlEditorField();  	//HTML编辑器
//			jQuery("input[moduleType='fileManager']").obpmFileManager();  		//文件管理功能
//			jQuery("input[moduleType='mapField']").obpmMapField();  				//地图功能
//			jQuery("input[moduleType='wordField']").obpmWordField();  			//Word编辑器
			jQuery("div[moduleType='flowHistoryField']").obpmFlowHistoryField();  			//流程历史控件
			jQuery("input[moduleType='weixingpsfield']").obpmWeixinGpsField();  			//微信gps控件
			jQuery("input[moduleType='surveyField']").obpmSurveyField();//问卷调查控件
			jQuery("input[moduleType='weixinrecordfield']").obpmWeixinRecord();
			jQuery("input[moduleType='qrcodefield']").obpmQRCodeField();//二维码控件
//			loadPedding();	//待办			
			//formOneRow();//表单显示为一行
		//},1);
	}catch(ex){
		alert(ex);
//		alert("表单构建失败，请重试或联系管理员");
	}
}
//--表单控件jquery重构--end--//



//--表单刷新重计算--start--//
var isRefreshLoading = false;	//是否正在执行刷新重计算，表单保存时使用
var dy_token = true;

function dy_getFormid() {
	return formid;
}

function dy_getValuesMap(withParentid) {
	jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
	var valuesMap = {};
	var mapVal = "";
	var mapVals = new Array();
	if(jQuery("#dy_refreshObj").size() > 0){
		mapVal = jQuery("#dy_refreshObj").attr("mapVal");
	}
	if(mapVal)
		mapVals = mapVal.split(";");
	if (document.getElementsByName('_selects')) {
		var selects = getCheckedListStr('_selects');
		valuesMap['_selectsText'] = (selects && selects != null) ? selects : '';
	}
	for(var i=0; i<mapVals.length; i++){
		valuesMap[mapVals[i]] = ev_getValue(mapVals[i]);
	}
	
	if ($("input[name='parentid']").length > 0
			&& withParentid) {
		valuesMap['parentid'] = $("input[name='parentid']").val();
	}
	if ($("input[name='application']").length > 0) {
		valuesMap['application'] = $("input[name='application']").val();
	}
	return valuesMap;
}


//文字和表单控件同一行显示
function formOneRow(){
	$("body").append("<div id='text-width' style='display:none'>"+$(this).text()+"</div>")
	var $title = $("#_formHtml label.field-title"); 
	$title.each(function(){
		if($(this).parent(".formfield-wrap[id^='weixinImageUpload']").size()==0){
			var $title_width = $("#text-width").width(); 
			if($title_width < $(window).width()/2){
				//alert($title_width+" "+$(window).width()/2);
				$(this).addClass("field-title-left");
				$(this).next().addClass("contactField-right");
				$(this).parent(".formfield-wrap").addClass("formfield-wrap-box");
		    }
		}
		$("#text-width").html("");
	})
}

function dy_refresh(actfield) {

	var $dy_refreshObj = jQuery("#dy_refreshObj");
	var formid = $dy_refreshObj.attr("formid");
	var docid = $dy_refreshObj.attr("docid");
	var userid = $dy_refreshObj.attr("userid");
	var flowid = $dy_refreshObj.attr("flowid");
	
	try {
		if (dy_token) {
			if(typeof FormHelper !="undefined"){
				dy_token = false;
				showLoadingToast();
				isRefreshLoading = true;
				if (document.getElementById('tabid')) {
					var _tabid = document.getElementById('tabid').value;
				}
				FormHelper.refresh(_tabid,
						formid, actfield,
						docid,
						userid,
						dy_getValuesMap(true), flowid, function(str) {
							try {
								eval(str);
								jQuery("#flowprocessDiv").hide();//隐藏流程面板，使得下次点击展开时能够进行重新加载。
								jqRefactor();
								if($(".refreshItem").find("iframe").size()>0){
									$(".refreshItem").find("iframe").each(function(){
										$(this).height($(this).parent("span").height());
									})
								}
								$(".refreshItem").css("height","auto").removeClass("refreshItem");
							} catch (ex) {
								alert('form: ' + ex.message);
							};
							hideLoadingToast();
							isRefreshLoading = false;
							dy_token = true;
						});
			}
		}
	} catch (ex) {
		hideLoadingToast();
		isRefreshLoading = false;
		alert('form: ' + ex.message);
	}
}
//--表单刷新重计算--end--//; 
﻿(function($){
	$.fn.obpmInputField =function(){
		return this.each(function(){
			var $field =jQuery(this);
			var id  = $field.attr("_id");
			var name = $field.attr("_name");
			var textType = $field.attr("_textType");
			var isBlank = $field.attr("_isBlank");
			var isBorderType = $field.attr("_isBorderType");
			var fieldType = $field.attr("_fieldType");
			var fieldKeyEvent = $field.attr("_fieldKeyEvent");
			var displayType = $field.attr("_displayType");
			var hiddenValue = $field.attr("_hiddenValue");
			var isRefreshOnChanged = $field.attr("_isRefreshOnChanged");
			var cssClass = $field.attr("_cssClass");
			var title = $field.attr("_title");
			var style = $field.attr("style");
			var subGridView = $field.attr("_subGridView");
			var discript = $field.attr("_discript");
			
			isBlank = (isBlank == "true");
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			var value = $field.attr("value");
			value = value ? value : "";
			discript = discript? discript : name;
			
			var html = "";
			if(displayType == PermissionType_HIDDEN || textType.toLowerCase() == "hidden"){
				html="<input type='hidden' name='" + name + "' id='" + id + "' value='" + value + "' />";
				this.parentNode.innerHTML = html;
			}else{
				if(value && value != ""){
					value = value.replace(/"/g, "&quot;") ;//替换空格符
				}else{
					if(fieldType == "VALUE_TYPE_NUMBER"){
						value = "0";
					}
				}
				if(!title){
					title = "";
				}else if(title != "")
					title = title.replace(/"/g, "&quot;") ;//替换空格符
				



				html += "<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";	//文本类型
				
				if(textType == "tel" && value.length>0){
					html +="<table width=\"100%\"><tr><td width=\"95%\">";
				}
				html += "<input class='contactField requiredField' type=";	//文本类型
				if(!isBlank){
					if(textType.toLowerCase() == "password"){
						html+="\"password\"";
					}else if(fieldType == "VALUE_TYPE_NUMBER"){	// 类型为数字时
						html+="\"number\"";
					}else if(textType == "tel"){
						html+="\"text\"";
					}else{
						html+="\"text\"";
					}
				}else{
					html+="\"text\"";
				}
				
				
				
				if(textType == "tel" && value.length>0){
					html+=" id=\"" + name + "\" name=\"" + name + "\" value=\"" + value + "\" style=\"width: 100%; height: 32px; border: 1px solid rgba(0,0,0,.2); border-radius:3px;\" data-enhance=\"false\"";
				}else if(textType == "tel"){
					html+=" id=\"" + name + "\" name=\"" + name + "\" value=\"" + value + "\" style=\"background:url(../../phone/resource/main/images/tel.png) no-repeat;background-position:right center; background-size:28px 28px;\" data-enhance=\"false\"";
				}else{
					html+=" id=\"" + name + "\" name=\"" + name + "\" value=\"" + value + "\" data-enhance=\"false\"";
				}
				
				if(isRefreshOnChanged){
					if(subGridView){
						html += " onchange=\"dy_view_refresh(this.id)\"";
					}else{
						html += " onchange=\"dy_refresh(this.id)\"";
					}
				}
				
				html+=" /></div>";
				if(textType == "tel" && value.length>0){
					html+="</td><td width=\"5%\"><a href=\"tel:" + value + "\"><span style=\"width:32px; height:32px; margin:5px; display:block;background:url(../../phone/resource/main/images/tel.png) no-repeat;  background-size:32px 32px;\"></span></a></td></tr></table>";
				}
				
				var $html = $(html);
				
				if(textType.toLowerCase() == "readonly" || displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED){
					if(isBorderType == "true"){
						$html.find("input").attr("type","hidden").css("display", "none");
						$html.append("<div id='"+ name +"_show' class='showItem'>" + value + "</div>");
					}else{
						$html.find("input").attr("readonly","readonly").css({"background-color":"rgb(239, 239, 239)","color":"#333"})//只读
					}
				}
				$html.bind("change",function(){
					if(isRefreshOnChanged){
						if(subGridView){
							dy_view_refresh(this.id);
						}else{
							dy_refresh(this.id);
						}
					}
				}).replaceAll($field);//.textinput();
			}
		});
	};
})(jQuery);; 
(function($) {
	$.fn.obpmButtonField = function() {
		return this
				.each(function() {
					var $btn = jQuery(this);
					var val = $btn.attr("value");
					var activityType = $btn.attr("activityType");

					// 29:批量签章;43:跳转
					// 嵌入视图不使用批量签章和跳转按钮
					if ((activityType == "29" || activityType == "43")
							&& typeof DisplayView == 'object'
							&& DisplayView.getTheView(this)) {
						$btn.remove();
						return;
					}
					/*
					 * activityType: 已完成： 新建:2; 删除:3; 提交:5; 返回:10; 保存并返回:11;
					 * 未完成： 查询Document:1; 保存并启动流程:4; SCRIPT处理:6; 保存并关闭窗口:9;
					 * 无:13; 打印:14; 连同流程图一起打印:15; 导出Excel:16; 保存并新建:17;
					 * 保存草稿不执行校验:19; 批量审批按钮:20; 保存并复制:21; 查看流程图:22; PDF导出:25;
					 * 文件下载:26; Excel导入:27; 电子签章:28; 批量电子签章:29; 跳转操作:32;
					 * 启动流程:33; 视图打印:36; 转发:37; Dispatcher:39; 保存并新建:42; 跳转:43;
					 * 归档:45; 不再使用： 修改Document:7; 关闭窗口:8;
					 * 保存并新建(新建有一条有旧数据Document):12; 清掉所有这个form的数据: 18;
					 * FLEX打印:30; FLEX带流程历史打印:31;
					 */

					switch (activityType) {
					case "2":
						activityCss = "btn-positive";
						break;
					case "3":
						activityCss = "btn-negative";
						break;
					case "4":
					case "42":
						activityCss = "btn-positive";
						break;
					case "5":
						activityCss = "";
						break;
					case "10":
						activityCss = "";
						break;
					case "11":
					case "34":
						activityCss = "btn-primary";
						break;
					default:
						activityCss = "";
						break;
					}
					if (activityType != "5"
							&& (activityType == 2 || activityType == 3
									|| activityType == 4
									|| activityType == 8
									|| activityType == 10
									|| activityType == 34
									|| activityType == 11 || activityType == 13
									|| activityType == 21 || activityType == 42 || activityType == 19)) {// 当按钮类型为5（流程处理）时，不做处理，具体渲染工作交由flowprcss.jsp完成
						var onclick = this.onclick;

						var $btnN = jQuery(
								"<a class='"+activityType+" btn " + activityCss
										+ " btn-block' data-transition='fade'>"
										+ val + "</a>").click(onclick);

						$btnN.insertAfter($btn);
						$btn.remove();
					}else if (activityType != "5") {
						$btn.remove();
					}
				});
	};
})(jQuery);; 
(function($){
	$.fn.obpmTextareaField =function(){
		return this.each(function(){
			var $field = jQuery(this);
			var id =$field.attr("_id");
			var value =$field.val();
			var name =$field.attr("_name");
			var cssClass =$field.attr("_cssClass");
			var classname =$field.attr("classname");
			var isRefreshOnChanged =$field.attr("_isRefreshOnChanged");
			var fieldType =$field.attr("_fieldType");
			var isBorderType =$field.attr("_isBorderType");
			var style=$field.attr("style");
			var displayType = $field.attr("_displayType");
			var hiddenValue = $field.attr("_hiddenValue");
			var subGridView = $field.attr("_subGridView");
			var discript = $field.attr("_discript");
			
			value = value ? value : "";
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			discript = discript? discript : name;
			
			var html="";
			var height = "";
			var width = "";
			if(displayType == PermissionType_HIDDEN){
				html += "<textarea style='display:none' name='" + name + "'>" + value + "</textarea>";
			}else if(displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED) {
				html += "<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";	//文本类型
				if(isBorderType == "true"){
					html += "<div id='"+ name +"_show' class='showItem'>" + value + "</div>";
				}
				
				html+= "<textarea rows='5' class='contactField requiredField' readonly='readonly' style='";
				if(isBorderType == "true"){
					html += ";display:none;"; 
				}
				html += "background-color:#fdfdfd;color: #333;' name='" + name + "' id='" + name + "' data-enhance='false'>" + value + "</textarea></div>";
			}else{
				html += "<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";	//文本类型
				html+="<textarea rows='5' class='contactField requiredField' ";
				html+="id=\"" + name + "\" name=\""+name+"\" ";
				if(isRefreshOnChanged){
					if(subGridView){
						html += " onchange=\"dy_view_refresh(this.id)\"";
					}else{
						html += " onchange=\"dy_refresh(this.id)\"";
					}
				}
				html+=" data-enhance='false'>";
				html+=value;
				html+="</textarea></div>";
			}
			$field.after(html);
			$field.remove();
		});
	};
})(jQuery);; 
(function($){
	$.fn.obpmCheckbox = function(){
			return this.each(function(){
				var $field = jQuery(this);
				var name = $field.attr('_name');
				var displayType = $field.attr('_displayType');
				var textType = $field.attr('_textType');
				var isRefreshOnChanged = $field.attr('_isRefreshOnChanged');
				var classname = $field.attr('classname');
				var cssClass = $field.attr('_class');
				var getLayout= $field.attr('_getLayout');
				var discript = $field.attr("_discript");
				var hiddenValue = $field.attr("_hiddenValue");
				var html="";
				
				discript = discript? discript : name;
				isRefreshOnChanged = (isRefreshOnChanged == "true");
				
				html += "<div class='formfield-wrap' ";
	
				if(displayType == PermissionType_HIDDEN){
					html += " style='display:none;'";
				}
				html += "><label class='field-title contact-name' for='" + name + "' >" + discript + "</label><div class='radio-box contactField requiredField'>";
				$field.find("span").each(function(i){
					var $div = jQuery(this);
					var value = $div.attr('value');
					var text = $div.attr('text');
					var checkedListSize = $div.attr('checkedListSize');
					var isDef = $div.attr('isDef');
					var checkedListContains = $div.attr('checkedListContains');
					var divHtml="";
					divHtml +="<div class='radio-list'><input data-enhance='false' type='checkbox' id='"+name+(i+1)+"' value=\"" + value + "\" text='" + text + "' name='" + name + "' ";
					
					if(textType && textType.toLowerCase() == "readonly" || displayType == PermissionType_DISABLED ||displayType == PermissionType_READONLY){
						divHtml +=" disabled='disabled'";
					}
					if(isRefreshOnChanged){
						divHtml +=" onclick='dy_refresh(this.id)'";
					}
					if(displayType == PermissionType_HIDDEN){
						divHtml += " style='display:none;'";
					}
					if(checkedListSize >0 && checkedListContains=='true' || isDef =='true'){
						divHtml +=" checked='true'";
					}
					divHtml +="/>";
					divHtml +="<label style='display:inline' for='"+name+(i+1)+"' ";
					if(displayType == PermissionType_HIDDEN){
						divHtml += " style='display:none;'";
					}
					divHtml += ">" + text + "</label>";
					if(getLayout !="" && getLayout.toLowerCase() == "vertical"){
						divHtml +="<br/>";
					}
					html += divHtml;
					html+="</div> ";
					
					
				});
				html+="</div></div>";
				
				if(displayType == PermissionType_HIDDEN){
					html += hiddenValue;
				}
				$field.after(html);
				$field.remove();
			});
	};
})(jQuery);; 
(function($){
	$.fn.obpmRadioField= function(){
		return this.each(function(){
			var $field = jQuery(this);
			var id = $field.attr("_id");
			var name =$field.attr('_name');
			var isRefreshOnChanged=$field.attr('_isRefreshOnChanged');
			var classname=$field.attr('classname');
			var cssClass=$field.attr('_cssClass');
			var displayType = $field.attr("_displayType");
			var textType = $field.attr("_textType");
			var valueList = $field.attr("_valueList");
			var discript = $field.attr("_discript");
			var hiddenValue = $field.attr("_hiddenValue");
			var getLayout= $field.attr('_getLayout');
			
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			discript = discript? discript : name;
			
			var html="";
			//if(displayType != PermissionType_HIDDEN){
				var defvalue ="";
				html += "<div class='formfield-wrap'";
				if(displayType == PermissionType_HIDDEN){
					html += " style='display:none;'";
				}
				html += "><label class='field-title contact-name' for='" + name + "' >" + discript + "</label><div class='radio-box contactField requiredField'>";
				$field.find("span").each(function(i){
					var $div = jQuery(this);
					var value = $div.attr('value');
					var get0ption = $div.attr('get0ption');
					var getValue = $div.attr('getValue');
					var isDef = $div.attr('isDef');
					isDef = (isDef == "true");

//					if(textType && textType.toLowerCase()=="readonly" || displayType == PermissionType_DISABLED || displayType == PermissionType_READONLY){
//						name += name + "$forshow" ;
//					}
					html+="<div class='radio-list'><input data-enhance='false' type='radio' id='" + name+(i+1) + "' value=\"" + value + "\" name='" + name + "' class='" + cssClass + "' ";
					if(displayType == PermissionType_HIDDEN){
						html += " style='display:none;'";
					}
					if(isRefreshOnChanged){
						html+=" isRefreshOnChanged='true' onclick='dy_refresh(this.id)'";
					}
					if(textType && textType.toLowerCase()=="readonly" || displayType == PermissionType_DISABLED || displayType == PermissionType_READONLY){
						html+=" disabled='disabled'";
					}
					if(valueList != "null"){
						if(valueList.split(";")[0]==getValue){
							defvalue = getValue;
							html+=" checked ='checked' ";
						}
					}else if(isDef){
						defvalue = getValue;
						html+=" checked ='checked' ";
					}
					html+=" />";
					html+="<label style='display:inline' for='" + name+(i+1) + "' ";
					if(displayType == PermissionType_HIDDEN){
						html += " style='display:none;'";
					}
					html+=">" + get0ption + "";
					html+="</label></div> ";
				});
				html+="</div></div>";
				$field.after(html);
			//}
			$field.remove();
		});
	};
})(jQuery);; 
(function($) {
	$.fn.obpmSelectField = function() {
		return this.each(function() {
			var $field = jQuery(this);
			var id = $field.attr("_id");
			var name = $field.attr("_name");
			var innerhtml = $field.children().html();
			var isRefreshOnChanged = $field.attr("_isRefreshOnChanged");
			var textType = $field.attr("_textType");
			var displayType = $field.attr("_displayType");
			var fieldType = $field.attr("_fieldType");
			var style =$field.attr("style");
			var subGridView = $field.attr("_subGridView");
			var discript = $field.attr("_discript");
			var hiddenValue = $field.attr("_hiddenValue");
			var onchange = $field.attr("onchange");

			isRefreshOnChanged = (isRefreshOnChanged == "true");
			subGridView = (subGridView == "true");
			discript = discript? discript : name;
			
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			var html = "";
			var htmlShow = "";
			if(onchange){
				$field.removeAttr("onchange");
				if(onchange.indexOf("'") >= 0 || onchange.indexOf('"') >= 0){
					alert("onchange值不能包含引号！");
					onchange = "";
				}
			}else{
				onchange = "";
			}
			if(subGridView){
				$field.removeAttr("subGridView");
			}

			html += "<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "' >" + discript + "</label>";
			html += "<div class='select-box'";

			if(displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" 
				||displayType == PermissionType_DISABLED ||textType.toLowerCase() == "disabled"){
				html += " style='display:none;'";
			}
			html += "><select class='contactField requiredField'";
			html += " id='" + name + "' data-enhance='false' name='" + name + "' fieldType='" + fieldType
					+ "' ";
			if (isRefreshOnChanged) {
				if(subGridView){
					onchange += ";dy_view_refresh(\"" + id + "\")";
				}else{
					onchange += ";dy_refresh(\"" + id + "\")";
				}
			}
			html += " onchange='" + onchange + "'";
			html += " style=\"";	//style
			if(style) html += style + ";";
			if (textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN) {
				html += " display:none;";
			}
			html += "\"";
			if(displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" 
				||displayType == PermissionType_DISABLED ||textType.toLowerCase() == "disabled"){
				html += " disabled";
				htmlShow += "<div id='"+ name +"_show' class='showItem'>";
			}
			html += ">";
			//option
			$field.find("span").each(function(){
				$option = jQuery(this);
				var isSelected = $option.attr("isSelected");
				var val = $option.attr("value");
				var text = $option.html();
				var cssClass= $option.attr("cssClass");
				isSelected = isSelected == "true"?true:false;
				val = val?val:"";
				var optHtml = "<option value=\"" + val + "\" class=\"" + cssClass + "\"";
				if(isSelected){
					optHtml += " selected=\"true\"";
					htmlShow += val;
				}
				optHtml += " >" + text + "</option>";
				html += optHtml;
			});
			if(displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" 
				||displayType == PermissionType_DISABLED ||textType.toLowerCase() == "disabled"){
				htmlShow += "</div>";
			}
			html += "</select><i class='icon icon-down'></i></div>";
			
			if(displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" 
				||displayType == PermissionType_DISABLED ||textType.toLowerCase() == "disabled"){
				html += htmlShow;
			}
			
			html += "</div>";
			if(displayType == PermissionType_HIDDEN){
				html += "<span>"+ hiddenValue +"</span>";
			}
			if (textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN) {
				$field.parent().hide();
			}else{
				$field.parent().show();
			}
			$field.parent().html(html).find("select").css("width","100%");//.css("height","2em");
		});
	};
})(jQuery);; 
(function($){
	$.fn.obpmDateField = function(){
		return this.each(function(){
			var $field = jQuery(this);
			var id=$field.attr("_id");
			var name=$field.attr("_name");
			var fieldType=$field.attr("_fieldType");
			var classname=$field.attr("classname");
			var cssclass=$field.attr("_class");
			var limit=$field.attr("limit");
			var value=$field.attr("value");
			var displayType=$field.attr("_displayType");
			var textType=$field.attr("_textType");
			var dateFmt=$field.attr("_dateFmt");
			var getPrevName=$field.attr("_getPrevName");
			var getPrevNameMinDate=$field.attr("_getPrevNameMinDate");
			var skin=$field.attr("_skin");
			var minDate=$field.attr("_minDate");
			var maxDate=$field.attr("_maxDate");
			var noPatternHms=$field.attr("_noPatternHms");
			var isRefreshOnChanged=$field.attr("_isRefreshOnChanged");
			var subGridView = $field.attr("_subGridView");
			var hiddenValue = $field.attr("_hiddenValue");
			var discript = HTMLDencode($field.attr("_discript"));
			var dateFormat, timeFormat, onBeforeShow;
			//var style = $field.attr("style");
			
			value = value ? value : "";
			discript = discript? discript : name;
			
			//按mobiscroll日期格式调整数据
			if(dateFmt == "yyyy" && dateFmt.length == 4){
				dateFormat = "yy";
				timeFormat = "";
				onBeforeShow = function (inst) { 
					inst.settings.wheels[0].length=1;
					inst.settings.wheels[1].length=0;
				}
			}
			if(dateFmt == "yyyy-MM" && dateFmt.length == 7){
				dateFormat = "yy-mm";
				timeFormat = "";
				onBeforeShow = function (inst) {
					inst.settings.wheels[0].length=2;
					inst.settings.wheels[1].length=0;
					
				}
			}
			if(dateFmt == "yyyy-MM-dd" && dateFmt.length == 10){
				dateFormat = "yy-mm-dd";
				timeFormat = "";
				onBeforeShow = function (inst) { 
					inst.settings.wheels[0].length=3;
					inst.settings.wheels[1].length=0;
	            }
			}
			if(dateFmt == "yyyy-MM-dd HH:mm" && dateFmt.length == 16){
				dateFormat = "yy-mm-dd";
				timeFormat = "HH:ii";
				onBeforeShow = function (inst) { 
					inst.settings.wheels[0].length=3;
					inst.settings.wheels[1].length=2;
				}
			}
			if(dateFmt == "yyyy-MM-dd HH:mm:ss" && dateFmt.length == 19){
				dateFormat = "yy-mm-dd";
				timeFormat = "HH:ii:ss";
				onBeforeShow = function (inst) { 
					inst.settings.wheels[0].length=3;
					inst.settings.wheels[1].length=2;
				}
			}
			if(dateFmt == "HH:mm:ss" && dateFmt.length == 8){
				dateFormat = "";
				timeFormat = "HH:ii:ss";
				onBeforeShow = function (inst) {}
			}
			
			//if(style){
			//	style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			//}
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			getPrevName = (getPrevName == "true");
			noPatternHms = (noPatternHms == "true");
			subGridView = (subGridView == "true");

			var html="";
			var htmlShow = "";
			if(displayType != PermissionType_HIDDEN){
				var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性
				
				function Wdate($dataField){
					//计算最小值 
					var $minDateObj = $("#"+getPrevNameMinDate);
					var _minDate = $minDateObj.val();
					var minDateFmt = $minDateObj.attr("dateFmt");

					if(_minDate && _minDate != ""){
						minDate = _minDate;
					}
					if(!_minDate || _minDate == "") minDate="1900-01-01 00:00:00";
					
					//传入的最小值统一格式
					if(minDateFmt == "yyyy" && trim(minDate).length == 4){
						minDate = trim(minDate)+"-01-01 00:00:00";
					}
					if(minDateFmt == "yyyy-MM" && trim(minDate).length == 7){
						minDate = trim(minDate)+"-01 00:00:00";
					}
					if(minDateFmt == "yyyy-MM-dd" && trim(minDate).length == 10){
						minDate = trim(minDate)+" 00:00:00";
					}
					if(minDateFmt == "yyyy-MM-dd HH:mm" && trim(minDate).length == 16){
						minDate = trim(minDate)+":00";
					}
					if(minDateFmt == "HH:mm:ss" && trim(minDate).length == 8){
						minDate = "1900-01-01 "+minDate;
					}
					
					
					//计算最大值
					var $subInput = jQuery("input[min_name='" + id + "']");
					var maxDateFmt = $subInput.attr("dateFmt");
					if($subInput.size() > 0){
						var _maxDate = "";
						var _dateName = "";
						$subInput.each(function(){
							var curVal = jQuery(this).val();
							var curName = jQuery(this).attr("name");
							if(curVal != ""){
								var cur_Date = new Date(curVal.replace(/-/g,"\/"));
								var max_Date = new Date(_maxDate.replace(/-/g,"\/"));
								_maxDate = (cur_Date > max_Date)?_maxDate:curVal;
								_dateName = (cur_Date > max_Date)?_dateName:curName;
							}
						});
						_maxDate
						maxDate = _maxDate;
						dateName = _dateName;
					}
					if(!maxDate || maxDate == ""){maxDate = "2099-12-31 23:59:59";}
					
					//传入的最大值统一格式
					if(maxDateFmt == "yyyy" && trim(maxDate).length == 4){
						maxDate = trim(maxDate)+"-01-01 00:00:00";
					}
					if(maxDateFmt == "yyyy-MM" && trim(maxDate).length == 7){
						maxDate = trim(maxDate)+"-01 00:00:00";
					}
					if(maxDateFmt == "yyyy-MM-dd" && trim(maxDate).length == 10){
						maxDate = trim(maxDate)+" 00:00:00";
					}
					if(maxDateFmt == "yyyy-MM-dd HH:mm" && trim(maxDate).length == 16){
						maxDate = trim(maxDate)+":00";
					}
					if(maxDateFmt == "HH:mm:ss" && trim(maxDate).length == 8){
						maxDate = "0000-00-00 "+maxDate;
					}
					
					var now_min_Date = new Date(minDate.replace(/-/g,"\/"));
					var now_max_Date = new Date(maxDate.replace(/-/g,"\/"));
					if(now_min_Date > now_max_Date){
						var starDate = jQuery("#"+getPrevNameMinDate).attr("name");
						alert(starDate +" 不能大于 " + dateName);
						return false;
					}
					
					//ios[xxxx-xx-xx]格式不识别 转时间格式为[xxxx,xx,xx] -- 20151126 by tank 
					var maxarr = maxDate.split(/[- :]/); 
					msMaxDate = new Date(maxarr[0], maxarr[1]-1, maxarr[2], maxarr[3], maxarr[4], maxarr[5]);
					var minarr = minDate.split(/[- :]/); 
					msMinDate = new Date(minarr[0], minarr[1]-1, minarr[2], minarr[3], minarr[4], minarr[5]);
					
					
					
					if(dateFmt == "HH:mm:ss" && dateFmt.length == 8){
						$dataField.unbind().mobiscroll().time({
						    timeFormat: timeFormat,
			                theme: 'ios', 
			                mode: 'mixed', 
			                display: 'bottom',
			                lang: 'zh', 
			                minDate: msMinDate?msMinDate:null,
					        maxDate: msMaxDate?msMaxDate:null,
					        stepMinute: 5 , 
			                onBeforeShow: onBeforeShow,
			                onSelect : function(){
			                	$dataField.unbind().bind("click",function(){
			                		Wdate(jQuery(this));
			                	});
			                }
						}).trigger("click");
					}else{
						$dataField.unbind().mobiscroll().datetime({
							dateFormat: dateFormat,
						    timeFormat: timeFormat,
			                theme: 'ios', 
			                mode: 'mixed',  
			                display: 'bottom', 
			                lang: 'zh', 
			                minDate: msMinDate?msMinDate:null,
			                maxDate: msMaxDate?msMaxDate:null, 
			                stepMinute: 5 ,
			                onBeforeShow:  onBeforeShow,
			                onSelect : function(){
			                	$dataField.unbind().bind("click",function(){
			                		Wdate(jQuery(this));
			                	});
			                }
						}).trigger("click");
					}
				
					return $dataField.bind("change",function(){
	                	if (isRefreshOnChanged) {
							subGridView ? dy_view_refresh($(this).attr("id")):dy_refresh($(this).attr("id"));
						}
	                });
				};
				
				if(textType.toLowerCase()=="hidden" ){
					html+="<input type='hidden'";
				}else{
					html+="<input class='contactField requiredField Wdate' type='text'";
				}

				html+=" id='" + id + "' value='"+value+"' limit='"+limit+"' name='"+name+"' classname='"+classname+"' fieldType='"+fieldType+"' class='"+cssclass+"'";
				html+=" isRefreshOnChanged='" + isRefreshOnChanged + "' dateFmt='" + dateFmt + "' " + otherAttrsHtml;

				//if(style){
				//	html+=" style='" + style + "'";
				//}
				
				if(getPrevNameMinDate) html += " min_name=\"" + getPrevNameMinDate +"\"";
				html += " style='width:100%;";
				if(displayType == PermissionType_READONLY || textType.toLowerCase()=="readonly" || displayType == PermissionType_DISABLED){
					html+="background-color:#fdfdfd;color: #333;' disabled tabIndex='-1";
					htmlShow += "<div id='"+ name +"_show' class='showItem'>" + value + "</div>";
				}
				html += "' ";	
				html+=" />";

				if(textType.toLowerCase()=="hidden" ){
					$field.replaceWith(html);
					return;
				}
				
				var $html = $(html);

				var $htmlT = $("<div class='formfield-wrap'></div>");
				var labelH = "<label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";
				$htmlT.append(labelH);
				
				if(displayType == PermissionType_READONLY || textType.toLowerCase()=="readonly" || displayType == PermissionType_DISABLED){
					$html.css("display","none");
				}else{	//可编辑
					if(!subGridView){
						$html.bind("blur",function(){
							if(typeof(dateCompareVal)=="function") dateCompareVal(this);	//普通表单时设置表单是否更改
						});
					}

					$html.bind("click",function(){
						Wdate(jQuery(this));
					});

				}
				$htmlT.append($html);
				$htmlT.append(htmlShow);
				
				$htmlT.replaceAll($field);
			}else{
				html += "<input type='hidden' name=\""+name+"\" value=\""+value+"\" />"+ hiddenValue +"" ;
				$(html).replaceAll($field);
			}
		});
	};
})(jQuery);; 
(function($) {
	var isNoDeleteFile = true;
	var isReloadFile = false;
	map = {};

	function FMFileInfo(file) {
		var webIndex = file.path.lastIndexOf("/");
		var urlIndex = file.path.lastIndexOf("_/uploads");

		this.webPath = file.path;
		if (urlIndex >= 0) {
			this.webPath = file.path.substring(urlIndex + 1);
		}

		this.realName = file.path.substring(webIndex + 1, file.path.length);
		this.showName = file.name;
		this.url = contextPath + this.webPath;
	}

	function File(name, path) {
		this.name = name;
		this.path = path;
	}

	function refreshUploadListSub(fileFullName, uploadListId, readonly,
			refresh, applicationid, opentype, subGridView, disabled) {
		if (fileFullName == "clear") {
			jQuery("#" + uploadListId).html(''); // 清空显示值
			// map[uploadListId] = "";
			jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1)).val("");
		} else {
			if (fileFullName != "") {
				var $fileContent = jQuery("#" + uploadListId);
				$fileContent.empty();
				$fileContent.append(getDivContent(fileFullName, uploadListId,
						readonly, refresh, applicationid, opentype,
						subGridView, disabled));
			} else {
				jQuery("#" + uploadListId).html('');
				jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1)).val("");
				// map[uploadListId] = '';
			}
			
			if(jQuery("#" + uploadListId).find(".showOptions").size()>0){
				jQuery("#" + uploadListId).parents(".upload-box").find("button[name='btnDelete']").removeAttr("disabled");
			}else{
				jQuery("#" + uploadListId).parents(".upload-box").find("button[name='btnDelete']").attr("disabled","disabled");
			}
		}
		// isNoDeleteFile = true;
		// isReloadFile = false;
	}

	// 文件附件
	function refreshUploadList(fileFullName, uploadListId, readonly, refresh,
			applicationid, opentype, subGridView, disabled) {
		refreshUploadListSub(fileFullName, uploadListId, readonly, refresh,
				applicationid, opentype, subGridView, disabled);
		if (refresh != 'false') {
			if (subGridView) {
				dy_view_refresh(uploadListId);
			} else {
				window.setTimeout("dy_refresh('"
						+ uploadListId.substring(uploadListId.indexOf("_") + 1)
						+ "')", 500);
			}
		}
	}

	// 显示文件列表
	function showUploadPic(index, id, webPath, applicationid) {
		var $picDiv = jQuery("#" + id + "pic" + index);
		if ($picDiv.size() != 0) {
			var url = encodeURI(encodeURI(contextPath
					+ "/portal/upload/fileInfor.action?applicationid="
					+ applicationid + "&fileFullName=" + webPath));
			jQuery.post(url, function(x) {
				// 提交成功回调
					var oldTitle = $picDiv.attr("title");
					$picDiv.attr("title", oldTitle + x);
				});
		}
	}

	// 获得显示列表
	function getDivContent(fileFullName, uploadListId, readonly, refresh,
			applicationid, opentype, subGridView, disabled) {
		var files = [];
		
		if(fileFullName){
			files = JSON.parse(fileFullName);
		}
		
		var divContent = '';
		divContent += '<div class="hidepic" id="' + uploadListId + 'showFileDiv"' + ' style="background:#fff;width:100%;text-align:left;left:0px;top:17px;"></div>';

		var tohtml = "";
		for ( var i = 0; i < files.length; i++) {
			var info2 = new FMFileInfo(files[i]);
			tohtml += '<span class="showOptions btn btn-default" style="text-align: left;display:block;position:relative;z-index:1;margin-bottom:5px;">';

			var flag = false;// 判断文档类型是否为OFFIC文档

			// 获取文件类型 根据类型给文件赋予前置图标
			var fileType = info2.showName.substring(
					info2.showName.lastIndexOf(".")).toLowerCase();
			if (fileType == ".doc" || fileType == ".docx") {
				tohtml += '<img style="height:16px;" border="0" src="../../../resource/image/word.gif"/>';
			} else if (fileType == ".xls" || fileType == ".xlsx") {
				tohtml += '<img style="height:16px;" border="0" src="../../../resource/image/excel.gif"/>';
			} else if (fileType == ".pdf") {
				tohtml += '<img style="height:16px;" border="0" src="../../../resource/image/pdf.gif"/>';
			} else if (fileType == ".jpg" || fileType == ".png"
					|| fileType == ".jpeg" || fileType == ".gif") {
				tohtml += '<img style="height:16px;" border="0" src="../../../resource/image/image.gif"/>';
			} else {
				tohtml += '<img style="height:16px;" border="0" src="../../../resource/image/empty.gif"/>';
			}
			//可在线预览的文档
			/**
			if (fileType == ".doc" || fileType == ".docx" || fileType == ".xls"
					|| fileType == ".xlsx" || fileType == ".pdf"
					|| fileType == ".txt" || fileType == ".rtf"
					|| fileType == ".et" || fileType == ".ppt"
					|| fileType == ".dps" || fileType == ".pot"
					|| fileType == ".pps" || fileType == ".wps"
					|| fileType == ".html" || fileType == ".htm") {
				DWREngine.setAsync(false);
				FormHelper.hasSwfFile(info2.webPath, info2.realName, function(
						result) {
					flag = result;
				});
				DWREngine.setAsync(true);
			}
			**/

			var title = info2.showName;
			var fileType = title.substring(title.lastIndexOf("."))
					.toLowerCase();
			var fileName = title.substring(0, title.lastIndexOf("."));
			if (fileName != null && fileName.length > 8) {
				title = fileName.substring(0, 7) + ".."
						+ fileName.charAt(fileName.length - 1) + fileType;
			}

			tohtml += '<a type="show" style="cursor:pointer;">' + title + '</a>';

			tohtml += '<div class="showdiv" style="display: inline-block;right: 0px;position: absolute;top: 0px;">';
			//tohtml += '<div style="text-align:center;margin:0px;padding:0px;"><img style="margin:0px;padding:0px;" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAcAAAAGCAYAAAAPDoR2AAAACXBIWXMAABYlAAAWJQFJUiTwAAAKTWlDQ1BQaG90b3Nob3AgSUNDIHByb2ZpbGUAAHjanVN3WJP3Fj7f92UPVkLY8LGXbIEAIiOsCMgQWaIQkgBhhBASQMWFiApWFBURnEhVxILVCkidiOKgKLhnQYqIWotVXDjuH9yntX167+3t+9f7vOec5/zOec8PgBESJpHmomoAOVKFPDrYH49PSMTJvYACFUjgBCAQ5svCZwXFAADwA3l4fnSwP/wBr28AAgBw1S4kEsfh/4O6UCZXACCRAOAiEucLAZBSAMguVMgUAMgYALBTs2QKAJQAAGx5fEIiAKoNAOz0ST4FANipk9wXANiiHKkIAI0BAJkoRyQCQLsAYFWBUiwCwMIAoKxAIi4EwK4BgFm2MkcCgL0FAHaOWJAPQGAAgJlCLMwAIDgCAEMeE80DIEwDoDDSv+CpX3CFuEgBAMDLlc2XS9IzFLiV0Bp38vDg4iHiwmyxQmEXKRBmCeQinJebIxNI5wNMzgwAABr50cH+OD+Q5+bk4eZm52zv9MWi/mvwbyI+IfHf/ryMAgQAEE7P79pf5eXWA3DHAbB1v2upWwDaVgBo3/ldM9sJoFoK0Hr5i3k4/EAenqFQyDwdHAoLC+0lYqG9MOOLPv8z4W/gi372/EAe/tt68ABxmkCZrcCjg/1xYW52rlKO58sEQjFu9+cj/seFf/2OKdHiNLFcLBWK8ViJuFAiTcd5uVKRRCHJleIS6X8y8R+W/QmTdw0ArIZPwE62B7XLbMB+7gECiw5Y0nYAQH7zLYwaC5EAEGc0Mnn3AACTv/mPQCsBAM2XpOMAALzoGFyolBdMxggAAESggSqwQQcMwRSswA6cwR28wBcCYQZEQAwkwDwQQgbkgBwKoRiWQRlUwDrYBLWwAxqgEZrhELTBMTgN5+ASXIHrcBcGYBiewhi8hgkEQcgIE2EhOogRYo7YIs4IF5mOBCJhSDSSgKQg6YgUUSLFyHKkAqlCapFdSCPyLXIUOY1cQPqQ28ggMor8irxHMZSBslED1AJ1QLmoHxqKxqBz0XQ0D12AlqJr0Rq0Hj2AtqKn0UvodXQAfYqOY4DRMQ5mjNlhXIyHRWCJWBomxxZj5Vg1Vo81Yx1YN3YVG8CeYe8IJAKLgBPsCF6EEMJsgpCQR1hMWEOoJewjtBK6CFcJg4Qxwicik6hPtCV6EvnEeGI6sZBYRqwm7iEeIZ4lXicOE1+TSCQOyZLkTgohJZAySQtJa0jbSC2kU6Q+0hBpnEwm65Btyd7kCLKArCCXkbeQD5BPkvvJw+S3FDrFiOJMCaIkUqSUEko1ZT/lBKWfMkKZoKpRzame1AiqiDqfWkltoHZQL1OHqRM0dZolzZsWQ8ukLaPV0JppZ2n3aC/pdLoJ3YMeRZfQl9Jr6Afp5+mD9HcMDYYNg8dIYigZaxl7GacYtxkvmUymBdOXmchUMNcyG5lnmA+Yb1VYKvYqfBWRyhKVOpVWlX6V56pUVXNVP9V5qgtUq1UPq15WfaZGVbNQ46kJ1Bar1akdVbupNq7OUndSj1DPUV+jvl/9gvpjDbKGhUaghkijVGO3xhmNIRbGMmXxWELWclYD6yxrmE1iW7L57Ex2Bfsbdi97TFNDc6pmrGaRZp3mcc0BDsax4PA52ZxKziHODc57LQMtPy2x1mqtZq1+rTfaetq+2mLtcu0W7eva73VwnUCdLJ31Om0693UJuja6UbqFutt1z+o+02PreekJ9cr1Dund0Uf1bfSj9Rfq79bv0R83MDQINpAZbDE4Y/DMkGPoa5hpuNHwhOGoEctoupHEaKPRSaMnuCbuh2fjNXgXPmasbxxirDTeZdxrPGFiaTLbpMSkxeS+Kc2Ua5pmutG003TMzMgs3KzYrMnsjjnVnGueYb7ZvNv8jYWlRZzFSos2i8eW2pZ8ywWWTZb3rJhWPlZ5VvVW16xJ1lzrLOtt1ldsUBtXmwybOpvLtqitm63Edptt3xTiFI8p0in1U27aMez87ArsmuwG7Tn2YfYl9m32zx3MHBId1jt0O3xydHXMdmxwvOuk4TTDqcSpw+lXZxtnoXOd8zUXpkuQyxKXdpcXU22niqdun3rLleUa7rrStdP1o5u7m9yt2W3U3cw9xX2r+00umxvJXcM970H08PdY4nHM452nm6fC85DnL152Xlle+70eT7OcJp7WMG3I28Rb4L3Le2A6Pj1l+s7pAz7GPgKfep+Hvqa+It89viN+1n6Zfgf8nvs7+sv9j/i/4XnyFvFOBWABwQHlAb2BGoGzA2sDHwSZBKUHNQWNBbsGLww+FUIMCQ1ZH3KTb8AX8hv5YzPcZyya0RXKCJ0VWhv6MMwmTB7WEY6GzwjfEH5vpvlM6cy2CIjgR2yIuB9pGZkX+X0UKSoyqi7qUbRTdHF09yzWrORZ+2e9jvGPqYy5O9tqtnJ2Z6xqbFJsY+ybuIC4qriBeIf4RfGXEnQTJAntieTE2MQ9ieNzAudsmjOc5JpUlnRjruXcorkX5unOy553PFk1WZB8OIWYEpeyP+WDIEJQLxhP5aduTR0T8oSbhU9FvqKNolGxt7hKPJLmnVaV9jjdO31D+miGT0Z1xjMJT1IreZEZkrkj801WRNberM/ZcdktOZSclJyjUg1plrQr1zC3KLdPZisrkw3keeZtyhuTh8r35CP5c/PbFWyFTNGjtFKuUA4WTC+oK3hbGFt4uEi9SFrUM99m/ur5IwuCFny9kLBQuLCz2Lh4WfHgIr9FuxYji1MXdy4xXVK6ZHhp8NJ9y2jLspb9UOJYUlXyannc8o5Sg9KlpUMrglc0lamUycturvRauWMVYZVkVe9ql9VbVn8qF5VfrHCsqK74sEa45uJXTl/VfPV5bdra3kq3yu3rSOuk626s91m/r0q9akHV0IbwDa0b8Y3lG19tSt50oXpq9Y7NtM3KzQM1YTXtW8y2rNvyoTaj9nqdf13LVv2tq7e+2Sba1r/dd3vzDoMdFTve75TsvLUreFdrvUV99W7S7oLdjxpiG7q/5n7duEd3T8Wej3ulewf2Re/ranRvbNyvv7+yCW1SNo0eSDpw5ZuAb9qb7Zp3tXBaKg7CQeXBJ9+mfHvjUOihzsPcw83fmX+39QjrSHkr0jq/dawto22gPaG97+iMo50dXh1Hvrf/fu8x42N1xzWPV56gnSg98fnkgpPjp2Snnp1OPz3Umdx590z8mWtdUV29Z0PPnj8XdO5Mt1/3yfPe549d8Lxw9CL3Ytslt0utPa49R35w/eFIr1tv62X3y+1XPK509E3rO9Hv03/6asDVc9f41y5dn3m978bsG7duJt0cuCW69fh29u0XdwruTNxdeo94r/y+2v3qB/oP6n+0/rFlwG3g+GDAYM/DWQ/vDgmHnv6U/9OH4dJHzEfVI0YjjY+dHx8bDRq98mTOk+GnsqcTz8p+Vv9563Or59/94vtLz1j82PAL+YvPv655qfNy76uprzrHI8cfvM55PfGm/K3O233vuO+638e9H5ko/ED+UPPR+mPHp9BP9z7nfP78L/eE8/sl0p8zAAAAIGNIUk0AAHolAACAgwAA+f8AAIDpAAB1MAAA6mAAADqYAAAXb5JfxUYAAABmSURBVHjaRMyhDoJgGIXhB0cx6OZm8QI0k0hocKNootBosnGHXgR0rgSKxfL9401n5z072Tq/BU9UGPGFnY0XBtSpSLLAHRc8UCZ5wgfXGN7Q45yjQYdjyANaTHlcLPhhxT5y+R8AXjUNk8B1UFEAAAAASUVORK5CYII="/></div>';
			tohtml += '<div style="white-space:nowrap;padding:3px">';

			// true为打开office预览 false为普通预览
			if (flag) {
				tohtml += '<span type="showOffice" style="cursor:pointer;margin:2px;font-size:12px;" onselectstart="return false;">预览</span>';
			} else {
				if (fileType == ".jpg" || fileType == ".png"
						|| fileType == ".jpeg" || fileType == ".gif"
						|| fileType == ".ceb") {
					tohtml += '<span type="showPreview" style="cursor:pointer;font-size: 14px;padding: 5px;display: inline-block;" onselectstart="return false;"><a href="#" url="' + files[i].path + '" >预览</a></span>';
				}
			}
			tohtml += '<span type="showDownLoad" style="cursor:pointer;font-size: 14px;padding: 5px;display: inline-block;" onselectstart="return false;">下载</span>';
			if (!readonly) {
				tohtml += '<span type="showDelete" style="cursor:pointer;font-size: 14px;padding: 5px;display: inline-block;" onselectstart="return false;">删除</span>';
			}

			tohtml += '</div>';
			tohtml += '</div>';
			tohtml += '</span>';
		}

		var $fileContent = jQuery("#" + uploadListId).html(divContent);

		$fileContent.find('.hidepic').html(tohtml);
		var timeout1 = "";

		$fileContent.find(".showOptions").each(function(index) {
			$(this).bind("click mouseover",function() {
				//$(".showOptions").css("z-index", "1");
				//var $div = $(this).css("z-index", "100").children(".showdiv");
				//var aaW = $(this).width();
				//var zzW = $div.width();
				//var aaH = $(this).height();
				//var left = (aaW - zzW) / 2;
				//$div.css("top", aaH - 10);
				//$div.css("left", left);
				//timeout1 = setTimeout(function(){
					//$div.stop().show();
				//},300);
			}).bind("mouseout",function(){
				//window.clearTimeout(timeout1);
			});
		});

		$fileContent.find(".showdiv").each(
			function(index) {
				$(this).mouseover(function() {
					$(this).stop().show();
				});
				$(this).mouseout(function() {
					//$(this).stop().fadeOut(300);
				});
				$(this).find("span").each(
					function(index2) {
						$(this).mousedown(
							function(e) {
								$(this).css("background","#efe4b3");
								var type = $(this).attr("type");
								var file = new FMFileInfo(files[index]);
								if (type == "showPreview") {
									var $preview = $(this).find("a");
									var url = file.webPath;
									var showName = file.showName;
									var webPath = contextPath + url;
									var fileType = showName.substring(showName.lastIndexOf(".")).toLowerCase();
									if (fileType == ".png" 
										|| fileType == ".jpg"
										|| fileType == ".gif"
										|| fileType == ".jpeg") {
										showImageEffect(webPath);
									} else if (fileType == ".ceb") {
										previewInNewTab(webPath,showName);
									} else {
										$preview.target = "_blank";
										$preview.href = webPath;
										$preview.triggerHandler("click");
									}
								} else if (type == "showDownLoad") {
									var showName = file.showName;
									var webPath = file.webPath;
									downloadFile(showName,webPath);
								} else if (type == "showDelete") {
									deleteOneFile(index,uploadListId,refresh,
										applicationid,opentype,subGridView,disabled);
								} else if (type == "showOffice") {// 在线预览OFFIC文档
									previewFile(file.showName,file.webPath);
								}
								$(this).css("background","#ffffff");
								//$(this).parent().parent().hide();
							});
					});
				});

		$fileContent.find("a[type='deleteOne']").each(
				function(index) {
					$(this).click(
							function() {
								deleteOneFile(index, uploadListId, refresh,
										applicationid, opentype, subGridView,
										disabled);
							});
				});
		$fileContent.find("a[type='showpic']").each(
				function(index) {
					showUploadPic(index, uploadListId, files[index].path,
							applicationid);
				});

		$fileContent.find("a[type='showword']").each(function(index) {
			$(this).click(function() {
				var showName = $(this).attr("showName");
				var url = $(this).attr("url");
				showFileDialog(this, showName, url, readonly);
			});
		});

		// 当打开方式为弹出层时
		$fileContent.find(".showpic1").each(function() {
			$(this).click(function() {
				var showName = $(this).attr("showName");
				var url = $(this).attr("url");
				showFileDialog(this, showName, url, readonly);
			});
		});

		var time;

		$fileContent.mouseover(function() {
			if (time) {
				window.clearTimeout(time);
			}
		});

		divContent = "";

		//设置点击事件，点击时去除弹出框
		$(document).bind("mousedown",function(event){
			//$("div.showdiv").fadeOut(300);
		});
	}

	/**
	 * 文件预览
	 */
	function previewFile(fileName, path) {
		var url = encodeURI(encodeURI(contextPath
				+ "/portal/phone/resource/component/upload/preview.jsp?fileName="
				+ fileName + "&path=" + path));
		var _tmpwin = window.open(url, "_blank");
		_tmpwin.location.href = url;
	}

	/**
	 * 获取文字长度
	 */
	function GetCurrentStrWidth(text) {
		var currentObj = $('<pre>').hide().appendTo(document.body);
		$(currentObj).html(text);
		var width = currentObj.width();
		currentObj.remove();
		return width;
	}

	// 删除一个文件
	function deleteOneFile(index, id, refresh, applicationid, opentype,
			subGridView, disabled) {
		if (confirm("你确定删除当前文件吗？此操作不可恢复！")) {
			var files = [];
			var filefullname = "";
			var oldstr = jQuery("#" + id.substring(id.indexOf("_") + 1)).val();// 用于恢复数据

			var oField = jQuery("#" + id.substring(id.indexOf("_") + 1));
			files = JSON.parse(oField.val());
			var webpath = files[index].path;
			files.splice(index, 1);
			if (files.length > 0) {
				filefullname = JSON.stringify(files);
			}

			oField.val(filefullname);

			jQuery
					.ajax( {
						type : 'POST',
						async : false,
						url : encodeURI(encodeURI(contextPath
								+ "/portal/upload/deleteOne.action?fileFullName="
								+ webpath)),
						dataType : 'text',
						data : jQuery("#document_content").serialize(),
						success : function(x) {
							refreshUploadList(filefullname, id, false, refresh,
									applicationid, opentype, subGridView,
									disabled);
							filefullname = "";
						},
						error : function(x) {
							jQuery("#" + id.substring(id.indexOf("_") + 1))
									.val(oldstr);
						}
					});

		}
	}

	function refreshImgListSub(fileFullName, uploadListId, myheigh, mywidth,
			readonly, refresh, applicationid, opentype, subGridView) {
		if (jQuery.trim(fileFullName) != "" && fileFullName != "clear") {
			var image = JSON.parse(fileFullName);
			fileFullName = image[0].path;
			var divContent = '';

			var urlIndex = fileFullName.lastIndexOf("_/uploads");
			var url = fileFullName;
			if (urlIndex >= 0) {
				url = fileFullName.substring(urlIndex + 1);
			}

			divContent += '<div>';
			if (opentype == "dialog") {
				divContent += '<a id="'
						+ uploadListId
						+ 'pic0" href="#" class="showhidefilepic" '
						+ ' rel="lightbox" title="'
						+ fileFullName
								.substring(fileFullName.lastIndexOf("/") + 1)
						+ '; ">';
			} else {
				divContent += '<a id="'
						+ uploadListId
						+ 'pic0" href="#" href="'
						+ url
						+ '"  rel="lightbox" title="'
						+ fileFullName
								.substring(fileFullName.lastIndexOf("/") + 1)
						+ '" target="_blank">';
			}
			if (fileFullName.indexOf("pdf") == -1) {
				divContent += '<img  src="../../..' + url + '" width='
						+ mywidth + ' height=' + myheigh + ' border="0" '
						+ '/>';
			} else {
				divContent += '<font size=2 color=red>' + fileFullName
						.substring(fileFullName.lastIndexOf("/") + 1) + '</font>';
			}
			divContent += '</a>';
			divContent += '&nbsp;&nbsp;';
			divContent += '</div>';

			$content = jQuery("#" + uploadListId).html(divContent);
			$content.find(".showhidefilepic").click(
					function() {
						showFileDialog(this, fileFullName
								.substring(fileFullName.lastIndexOf("/") + 1),
								url, readonly);
					});

			$content.find(".showhidefilepic").each(function() {
				showUploadPic(0, uploadListId, fileFullName, applicationid);
			});

		} else {
			jQuery("#" + uploadListId).html('');
		}
	}

	// refresh iamge uploaded list
	function refreshImgList(fileFullName, uploadListId, myheigh, mywidth,
			readonly, refresh, applicationid, opentype, subGridView) {
		refreshImgListSub(fileFullName, uploadListId, myheigh, mywidth,
				readonly, refresh, applicationid, opentype, subGridView);
		if (refresh != 'false') {
			if (subGridView) {
				dy_view_refresh(uploadListId);
			} else {
				window.setTimeout("dy_refresh('"
						+ uploadListId.substring(uploadListId.indexOf("_") + 1)
						+ "')", 500);
			}
		}
	}

	/**
	 * 其他类别显示
	 */
	function showTextDialog(webPath,name) {
		OBPM.dialog.show( {
			opener : window.parent.parent,
			width : 1000,
			height : 580,
			url : webPath,
			args : {},
			title : name,
			close : function(result) {

			}
		});
	}
	
	function previewInNewTab(webPath){
		var _tmpwin = window.open(webPath, "_blank");
		_tmpwin.location.href = webPath;
	}

	// 弹出层来加载文件为了处理乱码问题
	function showFileDialog(obj, name, webPath, readonly) {
		var url = "";
		var fileType = name.substring(name.lastIndexOf(".")).toLowerCase();
		if (fileType == ".doc" || fileType == ".docx" || fileType == ".xls"
				|| fileType == ".xlsx") {
			url = contextPath + '/portal/dynaform/document/doViewWordFile.action';
			OBPM.dialog.show( {
				opener : window.parent.parent,
				width : 1000,
				height : 550,
				url : url,
				args : {
					"webPath" : contextPath + webPath,
					"readonly" : readonly
				},
				title : name,
				close : function(result) {

				}
			});
		} else {
			url = contextPath + webPath;
			obj.target = "_blank";
			obj.href = url;
			obj.triggerHandler("click");
		}
	}

	/**
	 * 图片预览窗口
	 * 
	 * url 图片路径
	 */
	function showImageEffect(url) {
		// 构造外围窗体 将窗体改为隐藏
		$("body").append('<div id="showImageDialog"></div>');
		var $div = $("#showImageDialog");
		$div.css( {
			"display" : "none",
			"position" : "fixed",
			"z-index" : "999",
			"top" : "0px",
			"right" : "0px",
			"bottom" : "0px",
			"left" : "0px"
		});
		$div
				.append('<div style="filter:alpha(opacity=70);-moz-opacity:0.7;-khtml-opacity: 0.7;position: fixed; z-index: 101; top: 0px; right: 0px; bottom: 0px; left: 0px; opacity: 0.7; background-color: rgb(0, 0, 0);"></div>');

		// 添加关闭按钮
		$div
				.append('<div id="imageClose" style="color: rgb(255, 255, 255); padding: 4px 10px ;  background-color: rgb(0, 0, 0);cursor: pointer;right: 0px;position: absolute;z-index:104;font-size:20px;">x</div>');
		// 添加图片外围div
		var imgdiv = '<div style="position: absolute; width: 300%; height: 100%; left: -100%;">';
		// 添加图片
		imgdiv += '<img style="position: absolute; z-index: 102; margin: auto; width: auto; top: 0px; right: 0px; bottom: 0px; left: 0px; display: block; cursor: pointer; max-width: 32%; max-height: 95%;" src="' + url + '"/>';
		imgdiv += "</div>";
		$div.append(imgdiv);

		// 鼠标滑进关闭按钮样式
		$("#imageClose").mouseover(function(e) {
			$("#imageClose").css("background-color", "rgb(55, 55, 55)");
		});
		$("#imageClose").mouseout(function(e) {
			$("#imageClose").css("background-color", "rgb(0, 0, 0)");
		});

		// 点击后淡出然后remove
		$("#imageClose").click(function(e) {
			$("#showImageDialog").fadeOut(50, function() {
				$div.remove();
			});
		});

		// 淡入显示
		$("#showImageDialog").fadeIn(50);
	}

	/**
	 * 前台删除文件
	 * 
	 * @param {}
	 *            valueField 文件路径保存字段
	 * @param {}
	 *            uploadListId
	 */
	function deleteFrontFile(valueField, uploadListId, applicationid) {
		// 删除URL
		if (confirm("你确定删除全部文件吗？此操作不可恢复！")) {
			var fileFullName = "";
			if (valueField.val() != '') {
				var files = JSON.parse(valueField.val());
				for ( var i = 0; i < files.length; i++) {
					fileFullName += files[i].path + ";";
				}
				if (fileFullName.length > 1) {
					fileFullName = fileFullName.substring(0,
							fileFullName.length - 1);
				}
			}
			this.url = contextPath
					+ "/portal/upload/delete.action?fileFullName="
					+ fileFullName;
			fileFullName = "";
			deleteFileCommon(valueField, uploadListId, url);
		}
	}

	/**
	 * 后台删除文件
	 * 
	 * @param {}
	 *            valueField 文件路径保存字段
	 * @param {}
	 *            uploadListId
	 */
	function deleteFile(valueField, uploadListId, applicationid) {
		// 删除URL
		this.url = contextPath + "/core/upload/delete.action?applicationid="
				+ applicationid + "&fileFullName=" + valueField.val();
		deleteFileCommon(valueField, uploadListId, url);
	}

	/**
	 * 前后台共有的删除文件方法
	 * 
	 * @param {}
	 *            valueField 文件路径保存字段
	 * @param {}
	 *            uploadListId
	 * @param {}
	 *            url 删除文件请求的路径
	 */
	function deleteFileCommon(valueField, uploadListId, URL) {
		// 保存原来的值用于删除发生异常时可以恢复数据
		var uploadListIdHTML = jQuery("#" + uploadListId).html();
		var uploadListIdValue = jQuery(
				"#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
				.val();

		jQuery("#" + uploadListId).html(''); // 清空显示值
		jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
				.val("");

		jQuery.ajax( {
			type : 'POST',
			async : false,
			url : encodeURI(url),
			dataType : 'text',
			data : jQuery("#document_content").serialize(),
			success : function(x) {
				if(jQuery("#" + uploadListId).find(".showOptions").size()<=0){
					jQuery("#" + uploadListId).parents(".upload-box").find("button[name='btnDelete']").attr("disabled","disabled");
				}
			},
			error : function(x) {
				// 恢复数据
			jQuery("#" + uploadListId).html(uploadListIdHTML);
			jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
					.val(uploadListIdValue);
		}
		});
		// jQuery("#" + uploadListId).html(''); // 清空显示值
		// valueField.val(''); // 清空值
		// map[uploadListId] = "";

	}

	/**
	 * 后台文件上传
	 * 
	 * @param {}
	 *            pathname 文件目录
	 * @param {}
	 *            pathFieldId 文件路径域ID
	 * @param {}
	 *            viewid 视图ID
	 * @param {}
	 *            allowedTypes 允许上传的类型
	 * @param {}
	 *            maximumSize 最大值
	 * @param {}
	 *            fileSaveMode 文件保存模式
	 * @return {} 文件网络路径
	 */
	function uploadFile(pathname, pathFieldId, viewid, allowedTypes,
			maximumSize, layer, fileSaveMode, applicationid) {
		var url = contextPath + '/core/upload/upload.jsp?path=' + pathname
				+ "&applicationid=" + applicationid;

		var oField = jQuery("#" + pathFieldId);
		OBPM.dialog.show( {
			opener : window.parent.parent,
			width : 800,
			height : 450,
			url : url,
			args : {},
			title : '文件上传',
			close : function(result) {
				if (result == null || result == undefined
						|| result == "undefined") {

				} else {
					if (oField != null) {
						oField.val(result);
					}
					return resutl;
				}
			}
		});
	}

	/**
	 * 前台文件上传
	 * 
	 * @param {}
	 *            pathname 文件目录
	 * @param {}
	 *            pathFieldId 文件路径域ID
	 * @param {}
	 *            viewid 视图ID
	 * @param {}
	 *            allowedTypes 允许上传的类型
	 * @param {}
	 *            maximumSize 最大值
	 * 
	 * @param {}
	 *            fileSaveMode 文件保存模式
	 * @param {}
	 *            callback 回调函数
	 * @return {} 文件网络路径
	 */
	function uploadFrontFile(title, pathname, pathFieldId, viewid,
			allowedTypes, maximumSize, fileSaveMode, callback, applicationid,
			limitNumber, fileType, customizeType) {
		hiddenDocumentFieldIncludeIframe();// in util.js
		var url = contextPath
				+ '/portal/phone/resource/component/upload/obpm.fileUpload.jsp?path=' + pathname;
		url = addParameters(pathname, pathFieldId, viewid, allowedTypes,
				maximumSize, fileSaveMode, url);
		url += '&applicationid=' + applicationid;
		url += '&limitNumber=' + limitNumber;
		url += '&fileType=' + fileType;
		url += '&customizeType=' + customizeType;
		OBPM.dialog.show( {
			url : url,
			args : {
				"webPath" : "aaaaaaaa",
				"readonly" : "222222"
			},
			title : title,
			close : function(result) {
				showDocumentFieldIncludeIframe();// //in util.js
				var oField = jQuery("#" + pathFieldId);
				var rtn = getReturnValue(oField, result);
				// 把上传的文件json格式 [{'name':'aaa.doc','path':'XXX'}] 放进文件上传控件的value
				var files = [];
				if (isReloadFile) {
					var fileStrs = result.split(";");
					if (oField && oField.val() && allowedTypes != 'image') {
						files = JSON.parse(oField.val());
					}
					for ( var i = 0; i < fileStrs.length; i++) {
						var fileStr = fileStrs[i].split(",");
						var file = new File(fileStr[0], fileStr[1]);
						files.push(file);
					}
				}
				if (files.length > 0) {
					rtn = JSON.stringify(files);
				}
				oField.val(rtn);
				if (callback && typeof (callback) == "function") {
					callback();
				}
			}
		});
	}
	/**
	 * 简单上传
	 * 
	 * @param {}
	 *            pathname 文件存放路径名称
	 * @param {}
	 *            pathFieldId 文件路径域ID
	 * @param {}
	 *            callback 回调函数
	 */
	function uploadFrontSimple(pathname, pathFieldId, applicationid) {
		var url = contextPath
				+ '/portal/phone/resource/component/upload/obpm.fileUpload.jsp?path=' + pathname
				+ '&applicationid=' + applicationid;
		var oField = jQuery("#" + pathFieldId);
		OBPM.dialog.show( {
			opener : window.parent.parent,
			width : 800,
			height : 450,
			url : url,
			args : {},
			title : 'upload',
			close : function(result) {
				if (result == null || result == undefined
						|| result == "undefined" || result == "clear") {
					oField.val('');
				} else {
					oField.val(result);
				}

			}
		});
		/*
		 * showfrontframe({ title : "upload", url : url, w : 650, h : 500,
		 * callback : callback });
		 */
	}

	/**
	 * 为URL添加参数
	 * 
	 * @param {}
	 *            pathname
	 * @param {}
	 *            pathFieldId
	 * @param {}
	 *            viewid
	 * @param {}
	 *            allowedTypes
	 * @param {}
	 *            maximumSize
	 * @param {}
	 *            fileSaveMode
	 * @param {}
	 *            url
	 * @return {}
	 */
	function addParameters(pathname, pathFieldId, viewid, allowedTypes,
			maximumSize, fileSaveMode, url) {
		var oField = jQuery("#" + pathFieldId);
		var oView = jQuery("#" + viewid);

		if (allowedTypes) {
			url += '&allowedTypes=' + allowedTypes;
		}

		if (maximumSize) {
			url += '&maximumSize=' + maximumSize;
		}

		if (fileSaveMode) {
			// 自定义
			url += '&fileSaveMode=' + fileSaveMode;
		} else {
			// 系统
			url += '&fileSaveMode=' + 00;
		}

		return url;
	}

	/**
	 * 获取返回值
	 * 
	 * @param {}
	 *            oField
	 * @return {}
	 */
	function getReturnValue(oField, rtn) {
		if (rtn == null || rtn == 'undefined') {
			isReloadFile = false;
			if (oField && oField.val()) {
				rtn = oField.val();
			} else {
				rtn = '';
			}
		} else {
			isReloadFile = true;
		}
		return rtn;
	}

	$.fn.obpmUploadField = function() {
		return this.each(function() {
			var $field = jQuery(this);
			var tagName = $field.attr("tagName");
			if(tagName=="ImageUploadField"){
				$field.obpmWeixinImageUpload();
				return;
			}
			var id = $field.attr("id");
			var disabled = $field.attr("disabled");
			var readonly = (disabled == 'disabled');
			var callbakFunction = $field.attr("callbakFunction");
			var path = $field.attr("path");
			var fileSaveMode = $field.attr("fileSaveMode");
			var application = $field.attr("application");
			var value = $field.attr("value");
			var name = $field.attr("name");
			var maxsize = $field.attr("maxsize");
			var uploadList = $field.attr("uploadList");
			var text = $field.html();
			var refreshOnChanged = $field.attr("refreshOnChanged");

			var uploadLabel = $field.attr("uploadLabel");
			var filelabel = $field.attr("filelabel");
			var deleteLabel = $field.attr("deleteLabel");
			var limitsize = $field.attr("limitsize");
			var limitNumber = $field.attr("limitNumber");
			
			var fileType = $field.attr("fileType");
			var customizeType = $field.attr("customizeType");
			var limitType = $field.attr("limitType");
			var opentype = $field.attr("opentype");

			var imgHeight = $field.attr("imgHeight");
			var imgWidth = $field.attr("imgWidth");

			var subGridView = $field.attr("subGridView");

			value = value ? value : "";
			subGridView = (subGridView == 'true');

			var html = "";
			var $uploadBox = $("<div class='upload-box'></div>");

			// td1
			//var $btnBox = $("<div style='display: inline-block;' ></div>");
			var $btnBox = $("<table style='width:100%'><tr></tr></table>");

			text = text ? text : "";
			
			if (subGridView) {
				var divHtml = "<div name='" + name + "_gridView' value='"
						+ value + "' fieldType='" + tagName
						+ "' style='display:none;'>" + text + "</div>";
				var $divHtml = $(divHtml);
				$divHtml.appendTo($btnBox);
			}
			
			// inputHidden
			var inputHtml = "<input type='hidden' name='" + name
					+ "' value='" + value + "' fieldType='" + tagName
					+ "' id='" + id + "'/>";
			var $inputHtml = $(inputHtml);
			$field.after($inputHtml);
			if(!readonly){//只读状态下不显示上传和下载按钮
				// uploadinput
				var uploadHtml = "<td style='padding-right:5px;position:relative;'><button class='btn btn-gray btn-block' data-inline='true' type='button'  value='"
					+ uploadLabel + "' name='btnSelectDept'";
				if (disabled && disabled != '') {
					uploadHtml += " disabled='" + disabled + "' ";
				}
				uploadHtml += "><span class='icon icon-share fileUpload_icon'></span> "
					+ uploadLabel 
					+ "</td>";
				//uploadHtml += "style='margin-right:5px' />";
				var $uploadHtml = $(uploadHtml);
				$uploadHtml.bind("click", function() {
					uploadFrontFile(filelabel + uploadLabel, path, id,
						'_viewid', limitType, maxsize,fileSaveMode, _callback, 
						application,limitNumber, fileType, customizeType);
				}).appendTo($btnBox);
	
				// deleteinput
				var delHtml = "<td style='padding-left:5px;position:relative;'><button disabled='disabled' class='btn btn-gray btn-block' data-inline='true' type='button' name='btnDelete'  value='"
						+ deleteLabel + "'";
				if (disabled && disabled != '') {
					delHtml += " disabled='" + disabled + "' ";
				}
				delHtml += "><span class='icon icon-trash fileDelect_icon'></span> "
						+ deleteLabel 
						+ "</button></td>";
				var $delHtml = $(delHtml);
				$delHtml.bind("click", 
				function() {
					deleteFrontFile(jQuery("#" + id), uploadList, application);
				}).appendTo($btnBox);
			}
			
			
			// image
			if (limitType == "image") {
				var td2Html = "<div style='display: block;' id='" + uploadList
						+ "' GridType='uploadFile'></div>";
				var $td2Html = $(td2Html);
				$td2Html.appendTo($btnBox);
			} else {
				var td2Html = "<div style='display: block;'><div  id='"
						+ uploadList
						+ "' GridType='uploadFile'></div></div>";
				var $td2Html = $(td2Html);
				$uploadBox.append($td2Html);
			}
			
			$btnBox.appendTo($uploadBox);
			
			// 描述
			if($field.attr("moduleType") != "uploadFileRefresh"){
				$field.attr("moduleType","uploadFileRefresh").css("display","none");
			}
			$field.after($uploadBox);

			var _callback = function() {

				if (limitType == 'image') {
					refreshImgList(jQuery("#" + id).val(), uploadList,
						imgHeight, imgWidth, readonly,refreshOnChanged, 
						application, opentype,subGridView);
				} else {
					refreshUploadList(jQuery("#" + id).attr("value"), uploadList,
						readonly, refreshOnChanged, application,opentype, subGridView, disabled);
				}
			}, init = function() {
				if (limitType == 'image') {
					refreshImgListSub(jQuery("#" + id).val(), uploadList,
						imgHeight, imgWidth, readonly,refreshOnChanged, 
						application, opentype,subGridView);
				} else {
					refreshUploadListSub(jQuery("#" + id).attr("value"), uploadList, 
						readonly, refreshOnChanged,
						application, opentype, subGridView, disabled);
				}
			};
			init();
		});
	};
	$.fn.obpmViewImageUpload = function() {
		return this
				.each(function() {
					var $field = jQuery(this);
					var imgw = $field.attr("imgw");
					var imgh = $field.attr("imgh");
					var viewReadOnly = $field.attr("viewReadOnly");
					var docId = $field.attr("docId");
					var docFormid = $field.attr("docFormid");
					var url = $field.attr("url");
					var fileName = $field.attr("fileName");

					viewReadOnly = (viewReadOnly == "true") ? true : false;
					var imgwHalf = imgh / 2;
					var isSubGridView = jQuery("#obpm_subGridView").size() > 0 ? true
							: false;

					var html = "<div  class='bigImg' style='position:relative;width:"
							+ imgw + "; height:" + imgh + "'>";
					if (viewReadOnly) {
						html += "<img alt='" + fileName + "' border='0' src='"
								+ url + "' width='" + imgw + "' height='"
								+ imgh + "' />";
					} else {
						html += "<a ";
						if (!isSubGridView) {
							html += " href=\"javaScript:viewDoc('" + docId
									+ "','" + docFormid + "')\"";
						}
						html += " title='" + fileName + "'>";
						html += "<img alt='" + fileName + "' border='0' src='"
								+ url + "' width='" + imgw + "' height='"
								+ imgh + "' />";
						html += "</a>";
					}
					html += "<div  class='smallIcon' style='display:none;position:absolute;right:0px;top:"
							+ imgwHalf
							+ "px;z-index:100;'><a class='imgClick' href='"
							+ url + "' target='blank'>";
					html += "<img alt='"
							+ fileName
							+ "' border='0' src='../../../resource/images/picture_go.png' title='点击查看原图' /></a><div>";
					html += "</div>";
					var $html = $(html);
					$html.mouseover(function(event) {
						event.stopPropagation();
						jQuery(this).find(".smallIcon").show();
					}).mouseout(function(event) {
						event.stopPropagation();
						jQuery(this).find(".smallIcon").hide();
					});
					$field.replaceWith($html);
				});
	};

})(jQuery);; 
(function($) {
	var ALBUM = false;
	
	function takePhoto(el){
		var $this = $(el);
		if($this.find(".pan").length>0) return;
		var id = $this.data("id");
		var oField = jQuery("#"+ id);
		var img = jQuery("#"+ id+"_img");
		var _wx = top.wx ? top.wx : wx;
		 _wx.chooseImage({
			 count: 1, // 默认9
			 sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
			 sourceType: ALBUM? ['album','camera']:['camera'], // 可以指定来源是相册还是相机，默认二者都有
		     success: function (res) {
		        var localIds = res.localIds;
		        setTimeout(function(){
		            _wx.uploadImage({
				        localId: localIds[0],
				        success: function (res) {
				          var serverId = res.serverId;
				          $.get(contextPath+"/portal/weixin/jsapi/upload.action",{"serverId":serverId},function(result){
					          if(result.status==1){
					        	  //todo 构建json
					        	oField.val(result.data);
								img.attr("src",localIds[0]).show();
					          }
					       });
				        },
				        fail: function (res) {
				          alert("网络异常，请再次尝试！");
				        }
				      });
		        }, 100)
			
		        //---
		        
		        
		      }
		    });
	}
	
	function previewImage(el){
		var $this = $(el);
		var current = $this.attr("src");
		if(current && current.length>0){
			var _wx = top.wx ? top.wx : wx;
			_wx.previewImage({
			    current: current, // 当前显示图片的http链接
			    urls: [] // 需要预览的图片http链接列表
			});
		}
	}
	$.fn.obpmTakePhoto = function() {
		return this.each(function() {
			// 微信
			$.cachedScript( "http://res.wx.qq.com/open/js/jweixin-1.0.0.js" ).done(function( script, textStatus ) {
				console.log( textStatus );
				$.cachedScript( "../../phone/resource/component/weixin/weixin.jsApi.js" ).done(function( script, textStatus ) {
				  console.log( textStatus );
				});
			});
			
			var $field = jQuery(this);
			var contextPath = $field.attr("contextPath");
			var value = $field.attr("value");
			var id = $field.attr("id");
			var imgw = $field.attr("imgw");
			var imgh = $field.attr("imgh");
			var name = $field.attr("name");
			var tagName = $field.attr("tagName");
			var discript = HTMLDencode($field.attr("discript"));
			var disabled = $field.attr("disabled");
			var displayType = $field.attr("displayType");
			ALBUM = $field.attr("album")=="true"; 
			
			var html = [];
			var $html = "";
			
			discript = discript? discript : name;
			
			if(displayType!=PermissionType_HIDDEN){
				var disabledClazz = "";
				var PhotoIcon = "&#xe60f;";
				if(displayType == PermissionType_DISABLED){
					disabledClazz = "ban";
					var PhotoIcon = "&#xe610;";
				}
				html.push('<div class="formfield-wrap"><label class="field-title contact-name" for="' + name + '">' + discript + '</label>');
				html.push('');
				html.push('<a data-id="'+id+'" class="btn btn-gray btn-block btn-photo"><i class="icon iconfont '+disabledClazz+'">'+ PhotoIcon +'</i></a>');
				html.push('<input type="hidden" id="' + id + '" name="' + name+ '" fieldType="' + tagName + '" value = "' +value+'"/>');
				html.push('<div class="btn-box-pic">');
				html.push('<img id="'+id+'_img" src="../../..' + value+'"');
				html.push(value.indexOf("photo.png")>=0? ' style="display:none;" >':' >');
				html.push('</div></div>');
				
				$html = $(html.join(""));
				$html.find(".btn-photo").bind("click",function(){
					takePhoto(this);
				}).end().replaceAll($field);
			}else{
				html.push('<input type="hidden" id="' + id + '" name="' + name+ '" fieldType="' + tagName + '" value = "' +value+'"/>');
				$html = $(html.join(""));
				$html.replaceAll($field);
			}
			$html.find("div.btn-box-pic").each(function () {  				
  				new RTP.PinchZoom($(this));
  			});
		});
	};
	$.fn.obpmViewTakePhoto = function(){
		return this.each(function(){
			var $field = jQuery(this);
			var imgw = $field.attr("imgw");
			var imgh = $field.attr("imgh");
			var viewReadOnly = $field.attr("viewReadOnly");
			var docId = $field.attr("docId");
			var docFormid = $field.attr("docFormid");
			var url = $field.attr("url");
			var fileName = $field.attr("fileName");
			
			viewReadOnly = (viewReadOnly=="true")?true:false;
			var imgwHalf = imgh/2;
			var isSubGridView = jQuery("#obpm_subGridView").size()>0?true:false;
			
			var html = "<div  class='takePhotoImg' style='position:relative;width:" + imgw + "; height:" + imgh + "'>";
			if(viewReadOnly){
				html += "<img alt='" + fileName + "' border='0' src='" + url + "' width='" + imgw + "' height='" + imgh + "' />";
			}else{
				html += "<a ";
				if(!isSubGridView){
					html +=" href=\"javaScript:viewDoc('" + docId + "','" + docFormid + "')\"";
				}
				html +=" title='" + fileName + "'>";
				html += "<img alt='" + fileName + "' border='0' src='" + url + "' width='" + imgw + "' height='" + imgh + "' />";
				html += "</a>";
			}
			html += "<div  class='takePhotoIcon' style='display:none;position:absolute;right:0px;top:"+imgwHalf+"px;z-index:100;'><a class='imgClick' href='" + url + "' target='blank'>";
			html += "<img alt='" + fileName + "' border='0' src='../../../resource/images/picture_go.png' title='点击查看原图' /></a><div>";
			html += "</div>";
			var $html=$(html);
			$html.mouseover(function(event) {
				event.stopPropagation();
				jQuery(this).find(".takePhotoIcon").show();
			}).mouseout(function(event){
				event.stopPropagation();
				jQuery(this).find(".takePhotoIcon").hide();
			});
			$field.replaceWith($html);
		});
	};
})(jQuery);; 

(function($){
	//表单Baidumap处理
	function FormBaiduMap(FieldID,applicationid,displayType){
		var oField = jQuery("#"+ FieldID);
		var url=contextPath+"/portal/share/component/map/form/baiduMap.jsp?type=dialog&applicationid="+applicationid+"&displayType="+displayType;
		hiddenDocumentFieldIncludeIframe();//in util.js
		OBPM.dialog.show({
			title : title_map,
			url : url,
			args: {"fieldID":FieldID,"mapData":oField.val()},
			width : 1000,
			height : 600,
			close : function(result) {
				showDocumentFieldIncludeIframe();////in util.js
				var rtn = result;
				if (result == null || result == 'undefined') {
					
				} else {
					oField.val(rtn);
				}
			}
	});
	}
	$.fn.obpmMapField= function(){
		return this.each(function(){
			var $field=jQuery(this);
			var id=$field.attr("id");
			var name=$field.attr("name");
			var fieldType=$field.attr("fieldType");
			var value=$field.attr("value");
			var mapLabel=$field.attr("mapLabel");
			var application=$field.attr("application");
			var displayType=$field.attr("displayType");
			var discript = HTMLDencode($field.attr("discript"));
			var srcEnvironment=$field.attr("srcEnvironment");
			var openType=$field.attr("openType");
			
			discript = discript? discript : name;
			
			if(displayType != PermissionType_HIDDEN){
				var hiddenHtml = "<input type='hidden' id='" + id + "' name='" + name + "' fieldType='" + fieldType + "'";
				if(value)
					hiddenHtml += " value='" + value + "'";
				
				hiddenHtml += " />";
				$field.after($(hiddenHtml));
				
				if(openType=='dialog'){
						var tableHtml = "<label for='" + name + "'>" + discript + "</label><table style='width:100%;height:100%;margin:0px;'>";
						tableHtml+="<tr><td style='border:0'>";
						tableHtml+="<input type='button' value='" + mapLabel + "' name='btnSelectDept'";
						if(displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED){
							tableHtml+=" disabled='disabled' ";
						}
						tableHtml +=" />";
//						if(discript !=""){
//							tableHtml +="<span style='border:0;margin-left:10px;'>" + discript + "</span>";
//						}
						tableHtml +="</td>";
						
						tableHtml+="</tr></table>";
						var $tablehtml = $(tableHtml);
						$tablehtml.find("input").click(function(){
							FormBaiduMap(id,application,displayType);
						});
						$field.after($tablehtml);
				}else{
					var paramname=encodeURI(encodeURI(id)); 
					var h=window.innerHeight/2;
					var iframeHtml = "<label for='" + name + "'>" + discript + "</label><iframe name='baidumap' id='baidumap' style='margin:0px;width:100%;height:"+h+"px;'";
					iframeHtml+="frameborder=0 ";
					iframeHtml+=" style=''";
					iframeHtml+="src=" + srcEnvironment + "/portal/share/component/map/form/baiduMap.jsp?type="+openType+"&fieldID="+paramname+"&applicationid="+application+"&displayType=" + displayType + "";
					iframeHtml+=" >";
					iframeHtml+="</iframe>";
//					if(discript !=""){
//						iframeHtml +="<span style='border:0;margin-left:10px;'>" + discript + "</span>";
//					}
					$field.replaceWith(iframeHtml);
				}
			}
			$field.remove();
		});
	};
	
})(jQuery);; 
(function($){
	$.fn.obpmDepartmentField = function(){
		return this.each(function(){
			var $field =jQuery(this);
			var id=$field.attr("_id");
			var name=$field.attr("_name");
			var fieldType=$field.attr("_fieldType");
			var classname=$field.attr("classname");
			var cssclass=$field.attr("class");
			var isRefreshOnChanged=$field.attr("_isRefreshOnChanged");
			var innerhtml = $field.html();
			var style=$field.attr("style");
			var displayType=$field.attr("_displayType");
			var textType=$field.attr("_textType");
			var subGridView = $field.attr("_subGridView");
			var hiddenValue = $field.attr("_hiddenValue");
			var discript = $field.attr("_discript");

			discript = discript ? discript : name;
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			subGridView = (subGridView == "true");
			
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			var html = "<div class='formfield-wrap' ";

			if(displayType == PermissionType_HIDDEN || textType.toLowerCase() == "hidden"){
				html += " style='display:none;' ";
			}
			html += ">";
			var htmlShow = "";
			//if(displayType != PermissionType_HIDDEN){
				var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性			
				html += "<label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";
				html += "<div class='select-box' ";

				if(textType.toLowerCase() == "readonly" || displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED){
					html += " style='display:none'";
					htmlShow += "<div id='"+ name +"_show' class='showItem'>" + $field.find("option:selected").text() + "</div>";
				}
				html += "><select class='contactField requiredField' id ='"+id+"' fieldType='"+fieldType+"'";
				
				html += " style=\"";	//style
				if(style) html += style + ";";
				html += "\"";
				
				html += "' classname='"+classname+"' class='contactField requiredField " + cssclass + "' name='"+name+"' ";
				html+=" isRefreshOnChanged='" + isRefreshOnChanged + "'" + otherAttrsHtml;
				
				if (isRefreshOnChanged){
					if(subGridView){
						html += " onchange='dy_view_refresh(this.id)'";
					}else{
						html += " onchange='dy_refresh(this.id)'";
					}
				}
				html+=">";
				html += "" + innerhtml;
				html += "</select><i class='icon icon-down'></i></div>" + htmlShow + "</div>";
				if(displayType == PermissionType_HIDDEN){
					html += "<span>"+ hiddenValue +"</span>";
				}
			//}
			this.parentNode.innerHTML = html;	
		});
	};
})(jQuery);; 
(function($){
	$.fn.obpmViewDialog =function(){
		return this.each(function(){
			var $field =jQuery(this);
			var name=$field.attr("name");
			var allow = $field.attr("allow");
			var mutil = $field.attr("mutil");
			var selectOne=$field.attr("selectOne");
			var parentid=$field.attr("parentid");
			var clsName = $field.attr("clsName");
			var formid = $field.attr("formid");
			var fieldid=$field.attr("fieldid");
			var isEdit=$field.attr("isEdit");
			var viewTitle=$field.attr("viewTitle");
			var dialogView=$field.attr("dialogView");
			var mapping=$field.attr("mapping");
			var openType=$field.attr("openType");
			var viewType=$field.attr("viewType");
			var divWidth=$field.attr("divWidth");
			var divHeight=$field.attr("divHeight");
			var isMaximization=$field.attr("isMaximization");
			var isRefreshOnChanged=$field.attr("isRefreshOnChanged");
			var caption=$field.attr("caption");
			var isDisable=$field.attr("isDisable");
			
			isDisable = (isDisable == "true");
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			isMaximization = (isMaximization == "true");
			selectOne = (selectOne == "true");
			mutil = (mutil == "true");
			isEdit = (isEdit == "true");
			
			var html="<button style='color:#767673' class='btn btn-gray btn-block' type=";
				html+="'button'";
				
			if (isDisable) {
				html+=" disabled='disabled'";
			}
			html+=" ><i class='icon iconfont'>&#xe617;</i> ";
			if(caption && caption.replace(/(^\s*)|(\s*$)/g, "").length > 0)
				html+= caption ;
			else
				html+="更多";
			
			html+="</button>";
			$(html).bind("click",function(){
				ViewHelper.convertValuesMapToPage(dy_getValuesMap(),function(text){
					var params= {
							allow:allow,
							mutil:mutil,
							selectOne:selectOne,
							parentid:parentid,
							className:clsName,
							formid:formid,
							fieldid:fieldid,
							isEdit:isEdit
					};
					var url = contextPath + '/portal/phone/dynaform/view/dialogTemp.jsp';
					url += '?_viewid=' + dialogView;
					url += '&datetime=' + new Date().getTime();
					url += '&' + jQuery.param(params,true);
					if(divWidth == null || divWidth == "" && isMaximization == false){
						url += '&_defaultSize=true';//后台显示大小为默认时，允许页面根据内容设置弹出层大小
					}
					var width = 640;
					var height = 400;
					if(divWidth != "" && divWidth.length > 0){
						width = divWidth;
					}
					if(divHeight != "" && divHeight.length > 0){
						height = divHeight;
					}
					
					OBPM.dialog.show({
								width : width, // 默认宽度
								height : height, // 默认高度
								url : url,
								args : {'html': new String(text), 'parent': window},
								maximization: isMaximization,
								maximized: true, // 是否支持最大化
								title : viewTitle,
								close : function(result) {
									//执行确定回调脚本
									if(!result) return;
									DWREngine.setAsync(false);
									var arr = jQuery("#document_content").serializeArray();
									var _params=new Object;  
						            $.each(arr,function(k,v){  
						            	_params[v.name]=v.value;  
						            });
						            _params = $.extend(_params,result.params? result.params:{});
						            _params["selectedValue"] = result.selectedValue ? result.selectedValue:"";
									ViewHelper.runViewDialogCallbackscript(_params,function(value){});
									DWREngine.setAsync(true);
									//映射字段
									var rtn = result.selectedValue;
										if(params.selectOne=="false"|| params.selectOne==false){
											getDialogValue(mapping, rtn);
										}else{
											getDialogSelectValue(mapping, rtn);
										}
									if(isRefreshOnChanged) {
										//刷新表单
										dy_refresh(name)
										};
								}
							});
				});
			}).replaceAll($field);
		});
	};
	
})(jQuery);; 
(function($){
	$.fn.obpmTreeDepartmentField =function(){
		return this.each(function(){
			var $field =jQuery(this);
			var textType=$field.attr("_textType");
			var fieldId = $field.attr("_fieldId");
			var fieldType = $field.attr("_fieldType");
			var cssClass=$field.attr("_cssClass");
			var fieldText=$field.attr("_fieldText");
			var limit = $field.attr("_limit");
			var isRefreshOnChanged = $field.attr("_isRefreshOnChanged");
			var name=$field.attr("_name");
			var fieldValue=$field.attr("_fieldValue");
			var title=$field.attr("_title");
			var style =$field.attr("style");
			var textFieldId = fieldId + "_text";
			var valueFieldId = fieldId;
			var displayType = $field.attr("_displayType");
			var treeTip = $field.attr("_treeTip");
			var subGridView = $field.attr("_subGridView");
			var discript = HTMLDencode($field.attr("_discript"));
			var hiddenValue = $field.attr("_hiddenValue");
			var discript = $field.attr("_discript");

			discript = discript? discript : name;
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			subGridView = (subGridView == "true");

			var html="";
			var htmlShow="";
			var readonly = false;
			//if(displayType != PermissionType_HIDDEN){
				var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性
				
				//文本框
				html += "<div class='formfield-wrap' style='position: relative;";
				if(textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN){
					html += ";display:none;";
				}
				html += "'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";
				html += "<input class='contactField requiredField' data-enhance='false' type='text' readonly ";
				html += " id='" + textFieldId + "'";
				html += " class='" + cssClass + "'";
				html += " fieldType='" + fieldType + "'";
				html += " value='" + fieldText + "'" + otherAttrsHtml;
				//html += " style=\"";	//style
				//if(style) html += style + ";";
				//html += "\"";
				if(displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED || textType.toLowerCase() == "readonly"){
					html += " style='display:none;'";
					htmlShow += "<div id='"+ name +"_show' class='showItem'>" + fieldText + "</div>";
				}
				html += " />";
				
				//隐藏域
				html += "<input type='hidden' id='" +valueFieldId + "'";
				html += " name='" + name + "'";
				html += " fieldType='" + fieldType + "'";
				html += " value='" + fieldValue + "'";
				html += " />";
				if(textType.toLowerCase() != "hidden" && displayType != PermissionType_HIDDEN){	
					//按钮
					if(displayType != PermissionType_READONLY && displayType != PermissionType_DISABLED && textType.toLowerCase()!="readonly"){
						html += "<span data-enhance='false' class='tree-department' ";
						var settings = "{textField:'" + textFieldId +
							"',valueField:'" + valueFieldId +
							"',limit:'" + limit +
							"',callback:" + (isRefreshOnChanged?(subGridView?"dy_view_refresh":"dy_refresh"):"''") + 
							",readonly:" + readonly + "}";
						html += " onclick=\"showDepartmentSelect('actionName'," + settings + ")\"";
						html += " title='" + treeTip + "'";
						html += "></span>";
					}
//					if(discript !=""){
//						html+="<span style='margin-left:10px;'>" + discript + "</span>";
//					}
				}
				if(displayType == PermissionType_HIDDEN){
					html += ""+ hiddenValue +"";
				}
				html += htmlShow + "</div>";
			//}
			this.parentNode.innerHTML = html;
		});
	};
})(jQuery);; 
(function($){
	$.fn.obpmUserfield = function(){
		return this.each(function(){
			var $field =jQuery(this);
			var id=$field.attr("_id");
			var name=$field.attr("_name");
			var fieldType=$field.attr("_fieldType");
			var cssClass=$field.attr("class");
			var value=$field.attr("value");
			var getFieldValue=$field.attr("_getFieldValue");
			var limitSum=$field.attr("_limitSum");
			var isRefreshOnChanged=$field.attr("_isRefreshOnChanged");
			var roleid=$field.attr("_roleid");
			var selectUser=$field.attr("_selectUser");
			var add=$field.attr("_add");
			var clearlabel=$field.attr("_clearlabel");
			var subGridView=$field.attr("_subGridView");
			var selectMode=$field.attr("_selectMode");
			var displayType=$field.attr("_displayType");
			var textType = $field.attr("_textType");
			var hiddenValue = $field.attr("_hiddenValue");
			var style = $field.attr("style");
			var discript = $field.attr("_discript");
			
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			fieldType = fieldType?fieldType:'';
			cssClass = cssClass?cssClass:'';
			getFieldValue = getFieldValue?getFieldValue:'';
			limitSum = limitSum?limitSum:'';
			isRefreshOnChanged = (isRefreshOnChanged == 'true');
			subGridView = (subGridView == 'true');
			roleid = roleid?roleid:'';
			selectUser = selectUser?selectUser:'';
			add = add?add:'';
			clearlabel = clearlabel?clearlabel:'';
			selectMode = selectMode?selectMode:'';
			displayType = displayType?displayType:'';
			textType = textType?textType:'';
			hiddenValue = hiddenValue?hiddenValue:'';
			style = style?"style='"+style+";'":'';
			value = value?value:'';
			discript = discript? discript : name;
			
			var readonly = false;
			var html="";
			var htmlShow = "";
			var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性
			
			if (displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" || displayType == PermissionType_DISABLED) {
				readonly= true;
				htmlShow += "<div id='"+ name +"_show' class='showItem'>" + value + "</div>";
			}
			html +="<div class='formfield-wrap'><input type='hidden' id='" + id + "' name='" + name + "' value='" + getFieldValue + "' fieldType='" + fieldType + "' />";
			if(textType.toLowerCase() == "hidden" || displayType == PermissionType_HIDDEN){
				html+="<input type='hidden'";
			}else{
				html += "<label class='field-title contact-name' for='" + name + "'>" + discript + "</label>";
				html+="<div class='userSelectBox'><input class='contactField requiredField userField' data-enhance='false' type='text' readonly ";
			}
			if(displayType == PermissionType_READONLY || displayType != PermissionType_MODIFY) html += " disabled='disabled'";
			//html+=style;
			html+=" isRefreshOnChanged='" + isRefreshOnChanged + "'";
			html+=" id='" + id + "_text' class='"+cssClass+"'  filetype='userfield' fieldtype='" + fieldType + "' name='" + name + "_text' " + otherAttrsHtml;
			if(value){
				html+=" value='" + value + "'";
				html+=" title='" + bulitTitle(value) + "'";
			}
			html+=" />";
			if(textType.toLowerCase() != "hidden" && displayType != PermissionType_HIDDEN){
				if(textType.toLowerCase() != "readonly" && displayType == PermissionType_MODIFY){
					var settings = "{textField:'" + id +
							"_text',valueField:'" + id +
							"',limitSum:'" + limitSum +
							"',selectMode:'" + selectMode +
							"',callback:'" + (isRefreshOnChanged?(subGridView?"dy_view_refresh(\"" + id + "\")":"dy_refresh(\"" + name + "\")"):"") + 
							"',readonly:" + readonly + "}";
					var clearStr = 'jQuery("#' + id + '_text").val("");'
							+ 'jQuery("#' + id + '_text").attr("title","");'
							+ 'jQuery("#' + id + '").val("");'
							+ (isRefreshOnChanged?(subGridView?'dy_view_refresh("'+ id +'")':'dy_refresh("' + name + '")'):'');
					html+="<span class='selectBtn icon icon-person' onclick=showUserSelectNoFlow('actionName'," + settings + ",'" + roleid + "')" +
							" title='"+selectUser+"'></span>";
					html+="<span class='selectBtn icon icon-trash' onclick='" + clearStr + "' title='"+ clearlabel + "'></span>";
				}
			}
//				if(discript !=""){
//					html+="<span style='margin-left:10px;'>" + discript + "</span>";
//				}
			if(displayType == PermissionType_HIDDEN){
				html += ""+ hiddenValue +"";
			}
			html+="</div>" + htmlShow + "</div>";

			var $html = $(html);
			if (displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" || displayType == PermissionType_DISABLED) {
				$html.find("input").css("display", "none");
			}
			//var $newField = $(html);
			//$newField.find("input").css("width","100%").css("height","2em");
			//$field.after($newField);
			$field.parent().html($html).find("input[type='text']").css("width","100%");
		});
	};
			
})(jQuery);

function bulitTitle(names){
	if(names != null && names != ''){
		var array = names.split(";");
		var title = '';
		for(var i = 0; i < array.length; i++){
			if(i != 0 && i%10 == 0){
				title += "\n";
			}
			title += array[i] + ";";
		}
		return title == ''?title:title.substring(0,title.length-1);
	}
	return '';
}; 
; 
; 
(function($){
	$.fn.obpmIncludedView = function(){
		return this.each(function(){
			if(!HAS_SUBFORM){
				HAS_SUBFORM=true;
				$("#form_tab").append('<a class="control-item swiper-slide active" href="#item1mobile" style="width:auto;">基本信息</a>')
				.show();
				$("#document_content").css("margin-top","36px");
			}
			var $field = jQuery(this);
			var id = $field.attr("id");
			var isRefreshOnChanged = $field.attr("isRefreshOnChanged");
			var getName = $field.attr("getName");
			var userType = $field.attr("userType");
			var action = $field.attr("action");
			var application = $field.attr("application");
			var _viewid = $field.attr("_viewid");
			var _fieldid = $field.attr("_fieldid");
			var _opentype = 227;//$field.attr("_opentype");
			var isRelate = $field.attr("isRelate");
			var parentid = $field.attr("parentid");
			var divid = $field.attr("divid");
			var getEditMode = $field.attr("getEditMode");
			var fixation = $field.attr("fixation");
			var fixationHeight = $field.attr("fixationHeight");
			if(fixation){
				height = "height:" + fixationHeight + ";";
			}else {
				height = "";
			}
			var $tabcontent = $field.parents(".tabcontent");
			if($tabcontent.html()){
				var istab = "true";
				var formid = $tabcontent.attr("formid");
				var tabVisibleStyle = "";
				if($tabcontent.is(":visible")){
					tabVisibleStyle = "display:block;";
				}else{
					tabVisibleStyle = "display:none;";
				}
			}else{
				var istab = "false";
			}		
			
			userType = (userType == "true");
			getEditMode = (getEditMode == "true");
			isRefreshOnChanged = (isRefreshOnChanged == "true");
			var html="";
				html+="<div id='" + _fieldid + "' isRelate='" + isRelate + "' istab='"+ istab +"' getName='" + getName + "' fixation='" + fixation + "' fixationHeight='" + fixationHeight 
					+ "' name='display_view' width='100%' height='100%' frameborder='0' style='' ";
				if(userType){
					html+=" src='../view/preView.action";//改成相对路径 --Jarod
					html+="?application=" + application + "";
					html+="&_skinType=" + $field.attr("skinType") + "";
				}else{
					html+=" src='../view/";
					html+="" + action + "";
					html+="?application=" + application + "";//改成相对路径 --Jarod
				}
				html+="&_viewid=" + _viewid + "";
				html+="&_fieldid=" + _fieldid + "";
				html+="&_opentype=" + _opentype + "";
				html+="&parentid=" + parentid + "";
				html+="&isRelate=" + isRelate + "";
				html+="&divid=" + divid + "";
				html+="&_from=includedView";
				if(getEditMode){
					html+="&isedit=false";
				}
				if(isRefreshOnChanged){
					html+="&refreshparent=true";
				}
				html+="'";
				html+=" isRefreshOnChanged='" + isRefreshOnChanged + "'";
				html+=" style='" + height + "overflow:auto;'></div>";
				
			var $html = $(html);
			var $tab = $("#tab_"+_fieldid);
			if($tab.length==0){
				$tab = $('<span id="tab_'+_fieldid+'" class="control-content"></span>');
				var $idTabInclude = $field.parent();
				if($idTabInclude.parent(".tabcontent").size()<=0){
					$("#form_tab").append('<a _tid='+formid+' class="control-item swiper-slide" href="#tab_'+_fieldid+'" style="width:auto;'+tabVisibleStyle+'">'+getName+'</a>');	
				}else{
					if($idTabInclude.parent().attr("ishidden")=="false"){
						$("#form_tab").append('<a _tid='+formid+' class="control-item swiper-slide" href="#tab_'+_fieldid+'" style="width:auto;'+tabVisibleStyle+'">'+getName+'</a>');	
					}
				}
				$("#form_continer").append($tab);
			}
			$html.load($html.attr("src"), function(){
				var $tabParameter = $(".tab_parameter");
				var viewid = _fieldid + "_params";
				var $viewDiv = $("<div></div>").attr("id",viewid).attr("_action",$(this).attr("src")).css("display","none");
				//表单元素转成span标签存入dom中
				$.each($(this).find(".tab_parameter").find("input[type!=button],select,textarea").serializeArray(),function(){
					$viewDiv.append("<span name='" + this.name + "'>" + this.value + "</span>"); 
				});
				$(this).append($viewDiv);
				//去掉表单元素
				$tabParameter.find("input[type!=button],select,textarea").each(function(){
					if(!$(this).attr("moduleType")) $(this).remove();
				});
				jqRefactor();					
			});
			
			$html.bind("refresh", function(){
				$html.load($html.attr("src"), function(){
					//alert("view refresh complated");
					var $tabParameter = $(".tab_parameter");
					var viewid = _fieldid + "_params";
					var $viewDiv = $("<div></div>").attr("id",viewid).attr("_action",$(this).attr("src")).css("display","none");
					//表单元素转成span标签存入dom中
					$.each($(this).find(".tab_parameter").find("input[type!=button],select,textarea").serializeArray(),function(){
						$viewDiv.append("<span name='" + this.name + "'>" + this.value + "</span>"); 
					});
					$(this).append($viewDiv);
					//去掉表单元素
					$tabParameter.find("input[type!=button],select,textarea").each(function(){
						if(!$(this).attr("moduleType")) $(this).remove();
					});
					dy_refresh(name);		
				});
			});
			$tab.html($html);
			$field.remove();
			
			

			var tabBox_width = 0;
			var tab_num = $("#form_tab").find(".control-item:visible").size();
			
			
			
			for(var i=0;i < tab_num;i++){
				$(".segmented-control").removeAttr("style");
				$("#form_tab").find(".control-item:visible:eq("+i+")").css({"width":"auto","display":"block"});
				tabBox_width = $("#form_tab").find(".control-item:visible:eq("+i+")").outerWidth()+tabBox_width;
			}
			
			$(".tab-box").width($(window).width());
			$(".segmented-control").removeAttr("style");
			if(tabBox_width <= $(window).width()){
				$(".segmented-control").css("display","table");
				$("#form_tab .control-item:visible").css("display","table-cell");
			}else{				
				$(".segmented-control").width(tabBox_width);
				$("#form_tab .control-item:visible").css("display","block");
				var swiper = new Swiper('.swiper-container', {
			        slidesPerView: 'auto',
			        spaceBetween: 0,
			        freeMode: true
			    });	
			}
			if($("#form_tab .control-item:visible").size()<=1){
				$("#form_tab").find("[_tid]").hide();
				$("#form_tab").find("a[href='#item1mobile']").hide();
				$("#document_content").css("margin-top","0px")
			}
			
		});
		
	};
})(jQuery);; 
(function($){
	$.fn.obpmPending=function(){
		return this.each(function(){
			var $field =jQuery(this);
			var id = $field.attr("id");
			var formId = $field.attr("formId");
			var summaryCfgId = $field.attr("summaryCfgId");
			var Summary = $field.attr("Summary");
			var isread = $field.attr("isread");
			isread =(isread=="true");
			var html="";
			
			var clsName = "";
			var pngName = "";
			
			if(isread){
				clsName = "pd";
				pngName = "16x16_0700/pencil";
				alt = "己读";
			}else{
				clsName = "pdread";
				pngName = "16x16_0380/email_edit";
				alt = "未读";
			}

			html+="<a title='" + alt + "' class='" + clsName + "' href=\"javaScript:viewDoc('" + id + "', '" + formId + "','" + summaryCfgId + "')\" " +
					"style='background:url(\"../../lib/icon/" + pngName + ".png" + "\") " +
							"no-repeat;display: block;background-position: 0 50%;padding-left: 20px;margin-bottom:5px;'>";
			html+="&nbsp;" + Summary + "&nbsp;";
			html+="</a>";
			jQuery(html).replaceAll($field);
				
		});
	};
})(jQuery);; 
(function($){
	$.fn.obpmWordField=function(){
		return this.each(function(){
			var $field = jQuery(this);
			var name = $field.attr("name");
			var id= $field.attr("id");
			var getItemValue = $field.attr("getItemValue");
			var getId = $field.attr("getId");
			var wordid = $field.attr("wordid");
			var getOpenTypeEquals = $field.attr("getOpenTypeEquals");
			var showWord = $field.attr("showWord");
			var secondvalue = $field.attr("secondvalue");
			var docgetId = $field.attr("docgetId");
			var docgetFormname = $field.attr("docgetFormname");
			var openType = $field.attr("openType");
			var displayType = $field.attr("displayType");
			var discript = HTMLDencode($field.attr("discript"));
			var saveable = $field.attr("saveable");
			var isSignature = $field.attr("isSignature");
			var filename = $field.attr("filename");
			var _docid = $field.attr("_docid");
			var _type = $field.attr("_type");
			var fieldname = $field.attr("fieldname");
			var formname = $field.attr("formname");
			var versions = $field.attr("content.versions");
			var application = $field.attr("application");
			var _isEdit = $field.attr("_isEdit");
			var signature = $field.attr("signature");
			var isOnlyRead = (displayType == PermissionType_READONLY || displayType == PermissionType_DISABLED);
			
			saveable = (saveable == "true");
			isSignature = (isSignature == "true");
			discript = discript? discript : name;
			
			var html="";
			if(displayType != PermissionType_HIDDEN){
				html += "<label for='" + name + "'>" + discript + "</label>";
				if(openType && openType=="2" || openType=="3"){
					if(getItemValue && getItemValue !=""){
						secondvalue = getItemValue;
					}else{
						getItemValue = wordid;
					}
					html+="<input type='hidden' name='" + name + "' id='" + id + "' value='" + getItemValue + "' />";
					html+="<input type='hidden' id='" + getId + "' value='" + secondvalue + "' />";
					html+="<button class='button-class' type='button'";
					if(displayType == PermissionType_READONLY){
						html +=" readonly";
					}
					html+=">";
					html+=" <img src='../../share/images/view/word.gif'></img>";
					html+="</button>";
//					if(discript !=""){
//						html+="<span style='margin-left:10px;'>" + discript + "</span>";
//					}
				}else{
					if (isOnlyRead) {
						_isEdit=1;
					} 
					if(getItemValue && getItemValue !=""){
						filename = getItemValue;
					}
					html+="<input type='hidden'  name='" + name + "' id='" + getId + "' value='" + getItemValue + "' />";
					html+="<iframe fieldName='" + name + "' id='" + getId + "' ";
					html+=" name='word' frameborder='0' width='100%' height='645px' scrolling='no' style='overflow:visible;z-index:-1px;' type='word' ";
					html+=" src='" + contextPath + "/portal/dynaform/document/newword.action?id=" + getId + "";
					html+="&filename=" + filename + "";
					html+="&_docid=" + _docid + "";
					html+="&_type=" + _type + "";
					html+="&fieldname=" + fieldname + "";
					html+="&formname=" + formname+"";
					html+="&content.versions=" + versions + "";
					html+="&application=" + application + "";
					html+="&_isEdit=" + _isEdit + "";
					html+="&isSignature=" + isSignature + "";
					html+="&isOnlyRead=" + isOnlyRead + "";
					html+="&signature=" + signature + "'";
					html+=" ></iframe>";
//					if(discript !=""){
//						html+="<span style='margin-left:10px;'>" + discript + "</span>";
//					}
				}
				if(openType && openType=="2" || openType=="3"){
					$(html).find("button").bind("click",function(){
						showWordDialog(showWord,
								'WordControl',
								docgetId,
								docgetFormname,
								secondvalue,
								id,
								'content.versions',
								openType,
								displayType,
								saveable,
								isOnlyRead,
								signature,
								(isSignature?true:false));
					}).end().replaceAll($field);
				}else{
					$(html).replaceAll($field);
				}
			}else{
				$(html).replaceAll($field);
			}
		});
	};
	
})(jQuery);; 
/**
 * 流程历史
 */
(function($){
	
	var ShowMode = {
			SHOW_MODE_TEXT : 'text',//只显示文本
			SHOW_MODE_DIAGRAM : 'diagram',//只显示图表
			SHOW_MODE_TEXT_AND_DIAGRAM : 'textAndDiagram'//显示文本与图表
	};
	
	function buildFlowHistoryTextList($field){
		//TODO html中的css样式代码抽离到css文件
		var html = [];
		html.push("<table cellSpacing='0' cellPadding='1' width='100%' align='center' style='border:solid #cccccc;border-width:1px 0px 0px 1px;'><tbody>");
		
		$field.find("table tr").each(function(i){//行<tr>
			var $trField = jQuery(this);
			if(i==0){//列头
				html.push("<thead>");
				html.push("<tr style='line-height:22px;'>");
				$trField.children().each(function(){
					var $tdField = jQuery(this);
					html.push("<th style='align:center;font-family: Arial;border:solid #cccccc;border-width:0px 1px 1px 0px;background-color:#EFF0F1;width="+$tdField.attr("width")+"' >");
					html.push($tdField.text()+"</th>");
					
				});
				html.push("</tr></thead>");
			}else{
				html.push("<tr style='line-height:22px;'>");
				$trField.children().each(function(){
					var $tdField = jQuery(this);
					//var textIndent = 30*$tdField.text().split("\\").length-1;
					html.push("<td style='font-family: Arial;border:solid #cccccc;border-width:0px 1px 1px 0px;align:center;'>");
					html.push($tdField.text()? $tdField.text() : "&nbsp;");
					if($tdField.hasClass('attitude') && $tdField.find("span").length==1){
						var data = $tdField.find("span").data("datas");
						html.push("<a href='");
						html.push("data:" + data['type'] + "," + data['data']);
						html.push("' target='_blank' title='查看大图' >");
						html.push("<img height='32px' src='");
						html.push("data:" + data['type'] + "," + data['data']);
						html.push("' >");
						html.push("</a>");
					}
					html.push("</td>");
					
				});
				html.push("</tr>");
			}
		});
		html.push("</tbody></table>");
		
		return html.join('');
	}
	
	
	$.fn.obpmFlowHistoryField = function(){
		return this.each(function(){
			var $field = jQuery(this);
//			$field.remove();//隐藏不渲染
			var showMode = $field.attr("showMode");
			var flowDiagram = $field.attr("flowDiagram");
			var mobile = $field.attr("mobile");
			var name = $field.attr("_name");
			var discript = $field.attr("_discript");
			discript = discript? discript : name;
			var html = [];
			if(mobile=="true"){
				html.push("<div style='list-style-type:none;margin-left: 3px;margin-right: 3px;margin-top: 3px;margin-bottom: 3px;'>");
				html.push("<label class='field-title contact-name' for='" + name + "'>" + discript + "</label>");
				if(showMode == ShowMode.SHOW_MODE_TEXT){
					html.push("<li>"+buildFlowHistoryTextList($field)+"</li>");
				}else if(showMode == ShowMode.SHOW_MODE_DIAGRAM){
					html.push("<li><img src='"+flowDiagram+"' style='width:100%' /></li>");
				}else if(showMode == ShowMode.SHOW_MODE_TEXT_AND_DIAGRAM){
					html.push("<li><img src='"+flowDiagram+"' style='width:100%' /></li>");
					html.push("<li>"+buildFlowHistoryTextList($field)+"</li>");
				}
				html.push("</div>");
			}
			$field.replaceWith($(html.join('')));
		});
	};
})(jQuery);

; 
;
/**
 * 微信gps位置控件
 * @author Happy
 * @param $
 */
(function($){
	
	/**
	 * 获取位置
	 */
	function getLocation(t,callback){
		wx.getLocation({
		    //type: 'gcj02', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
		    success: function (res) {
		       var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
		       var longitude = res.longitude ; // 经度，浮点数，范围为180 ~ -180。
		       
		       $.post(contextPath+"/attendance/attendance/convertGps2Baidu.action",{"from":"0","x":longitude,"y":latitude},function(result){
					if(1==result.status){
						var bdPoint = new BMap.Point(result.data.x,result.data.y);
						var geoc = new BMap.Geocoder();
			   			//坐标逆解析为地址
			   			geoc.getLocation(bdPoint, function(rs){
			   				var addComp = rs.addressComponents;
			   				var locationName = addComp.province+ addComp.city + addComp.district + addComp.street + addComp.streetNumber;
			   				var location ={
				    			   latitude:latitude,
				    			   longitude:longitude,
				    			   address:locationName
				    	   };
				    	   var options = t.data("weixinGpsField").options;
				    	   $("input[name='"+options.name+"']").val(JSON.stringify(location));
				    	   t.data("weixinGpsField").panel.find(".address-text").text(location.address);
				    	   if(callback && typeof callback=="function"){
				    		   callback(result);
				    	   }
			   			}); 
					}else{
					}
				},"json");
		    }
		});
		
	}
	
	/**
	 * 打开位置
	 */
	function openLocation(t){
		var v = t.val();
		if(!v || v.length<=0)return;
		
		var location = $.parseJSON(v);
		wx.openLocation({
		    latitude: location.latitude, // 纬度，浮点数，范围为90 ~ -90
		    longitude: location.longitude, // 经度，浮点数，范围为180 ~ -180。
		    name: '', // 位置名
		    address: location.address, // 地址详情说明
		    scale: 25, // 地图缩放级别,整形值,范围从1~28。默认为最大
		    infoUrl: '' // 在查看位置界面底部显示的超链接,可点击跳转
		});
		
			
	}
	/**
	 * gps控件初始化
	 */
	function _init(t){
		// 微信
		$.cachedScript( "http://res.wx.qq.com/open/js/jweixin-1.0.0.js" ).done(function( script, textStatus ) {
			console.log( textStatus );
			$.cachedScript( "../../phone/resource/component/weixin/weixin.jsApi.js" ).done(function( script, textStatus ) {
			  console.log( textStatus );
			});
		});

		var options = _parseOptions(t);
		var html = [];
		html.push('<div class="card_app">');
		html.push('<ul class="table-view table-address">');
		html.push('<li class="table-view-cell">');
		html.push('<a class="navigate-right location" data-transition="slide-in">');
		html.push('<span class="badge">');
		if(options.displayType==2){//可编辑
			html.push('<p class="refresh">获取位置</p>');
		}
		html.push('</span>');
		html.push('<div class="text"><i class="icon icon-add iconfont">&#xe613;</i><p class="add address-text"> &nbsp;</p></div>');
		html.push('</a>');
		html.push('</li>');
		html.push('</ul></div>');
		
		
		var panel = $(html.join(""));
		panel.insertAfter(t);
		var v = t.val();
		if(v.length<=0 && (options.displayType==2 || options.displayType==3)){//值为空，且可编辑状态下
			setTimeout(function(){
				getLocation(t,function(){
					//刷新表单
					if(options.isRefreshOnChanged){
						dy_refresh(options.id);
					}
				});
			}, 3000);
			
		}else{
			if(v && v!=""){
				var location = $.parseJSON(v);
				panel.find(".address-text").text(location.address);
			}
		}
		if(options.displayType==3){//隐藏
			panel.hide();
		}
		return panel;
	}
	
	/**
	 * 事件绑定
	 */
	function _bindEvents(t){
		var panel = t.data("weixinGpsField").panel;
		var options = t.data("weixinGpsField").options;
		panel.find(".refresh").on("click",function(e){
			e.preventDefault();
			e.stopPropagation();
			panel.find(".address-text").html(" &nbsp;");
			getLocation(t,function(){
				//刷新表单
				if(options.isRefreshOnChanged){
					dy_refresh(options.id);
				}
			});
		});
		panel.find(".location").on("click",function(e){
			e.preventDefault();
			e.stopPropagation();
			openLocation(t);
		});
	}
	
	/**
	 * 解析gps控件设置参数
	 */
	function _parseOptions(t){
		var options = {}
		options.id = t.attr("id");
		options.name = t.attr("name");
		options.displayType = t.attr("displayType");
		options.isRefreshOnChanged = t.attr("isRefreshOnChanged");
		return options;
	}
	
	$.fn.obpmWeixinGpsField = function(options, param){
		
		if(typeof options=="string"){
			return $.fn.obpmWeixinGpsField.method[options](this,param);
		}
		options = options || {};
		return this.each(function(){
			var t = $(this);
			var state = t.data("weixinGpsField");
			if(state){
				$.extend(state.options,options);
			}else{
				var r =_init(t);
				state = t.data('weixinGpsField', {
					options: $.extend({}, $.fn.obpmWeixinGpsField.defaults, _parseOptions(t), options),
					panel: r
				});
				
				_bindEvents(t);
			}
		});
	},
	
	$.fn.obpmWeixinGpsField.defaults = {
			id:'',
			isRefreshOnChanged:false,
			name:undefined
	},
	
	$.fn.obpmWeixinGpsField.method= {
			
		setValue:function(jq,value){
			
		},
		getValue:function(jq){
		}
	}
	
})(jQuery);
; 
;
/**
 * 调查问卷控件
 * @author Happy
 * @param $
 */
(function($){
	
	
	/**
	 * 控件初始化
	 */
	function _init(t){
		var options = _parseOptions(t);
		
		var v = t.val();
		var panel = $('<div id="survyfield_container_'+options.id+'" class="survyfield_container"></div>');
		
		var questions = options.questions;
		var displayType = options.displayType;
		for(i in  questions){
			var question = questions[i];
			var $q = _createQuestionPanle(question,displayType);
			panel.append($q);
			panel.append($('<div class="space-content"></div>'));
		}
		
		panel.insertAfter(t);
		return panel;
	}
	
	function _createQuestionPanle(question,displayType){
		var $q = $('<div class="question-content" id=""question-content-'+question.id+'"><div class="question-content-title"><b></b>'+question.topic+'</div></div>');
		var $a = _createAnswerPanle(question,displayType);
		$a.appendTo($q);
		return $q;
	}
	
	function _createAnswerPanle(question,displayType){
		var $a = $('<div class="question-content-answer"><table id="question-content-answer-'+question.id+'" width="100%" border="0" cellspacing="0" cellpadding="0"></table></div>');
		
		var disabled = displayType==4 || displayType==1? "disabled":""; //只读?
			
		
		for(i in question.options){
			var option = question.options[i];
			var type = option.type;
			var value = option.value;
			var text = option.text;
			
			var $o = undefined;
			switch (type) {
			case "checkbox":
			case "radio":
				if(option.custom){
					$o = $('<tr id="question-'+question.id+'-answerrow-'+i+'"><td style="height: 26px; padding-right: 10px;"><input id="question-'+question.id+'-answer-'+i+'" name="question-'+question.id+'-answer" ordernumber="'+i+'" type="'+type+'" value="" data-type="'+type+'" data-custom="true"  data-text="'+text+'" onclick="" '+disabled+'><label for="question-'+question.id+'-answer-'+i+'">'+text+'</label><input id="question-'+question.id+'-answer-value" name="question-'+question.id+'-answer-value" type="text" value="" '+disabled+'></td> </tr>');
				}else{
					$o = $('<tr id="question-'+question.id+'-answerrow-'+i+'"><td style="height: 26px; padding-right: 10px;"><input id="question-'+question.id+'-answer-'+i+'" name="question-'+question.id+'-answer" ordernumber="'+i+'" type="'+type+'" data-type="'+type+'" data-custom="false" data-text="'+text+'" value="'+value+'" onclick="" '+disabled+'><label for="question-'+question.id+'-answer-'+i+'">'+text+'</label></td> </tr>');
				}
				break;
			case "text":
				$o = $('<tr id="question-'+question.id+'-answerrow-'+i+'"><td style="height: 26px; padding-right: 10px;"><input id="question-'+question.id+'-answer-'+i+'" name="question-'+question.id+'-answer" ordernumber="'+i+'" type="'+type+'" data-type="'+type+'" data-custom="false" data-text="'+text+'" value="'+value+'" onclick="" '+disabled+'><label for="question-'+question.id+'-answer-'+i+'">'+text+'</label></td> </tr>');
				break;
			case "textarea":
				$o = $('<tr id="question-'+question.id+'-answerrow-'+i+'"><td style="height: 26px; padding-right: 10px;"><textarea id="question-'+question.id+'-answer-'+i+'" name="question-'+question.id+'-answer" ordernumber="'+i+'" data-type="'+type+'" data-custom="false" data-text="'+text+'" onclick="" '+disabled+'>'+value+'</textarea><label for="question-'+question.id+'-answer-'+i+'">'+text+'</label></td> </tr>');
				break;

			default:
				break;
			}
			$o.appendTo($a);
		}
		return $a;
	}
	
	/**
	 * 事件绑定
	 */
	function _bindEvents(t){
		//var panel = t.data("surveyField").panel;
	}
	
	/**
	 * 解析控件参数
	 */
	function _parseOptions(t){
		var options = {};
		options.id = t.attr("id");
		options.name = t.attr("name");
		options.displayType = t.data("displayType");
		options.discription = t.data("discription");
		options.questions = t.data("questions");
		return options;
	}
	
	/**
	 * 获取答案
	 */
	function _getValue(jq){
		var values =[];
		var state = jq.data("surveyField");
		var options = state.options;
		var panel = state.panel;
		
		var questions = options.questions;
		
		for(i in  questions){
			var question = questions[i];
			var value = {
				question:question.id,
				answers :[]
			}
			
			panel.find("input[name='question-"+question.id+"-answer']:checked").each(function(index){
				var $t = $(this);
				var type = $t.data("type");
				var custom = $t.data("custom");
				var text = $t.data("text");
				var v = custom? $t.parent().find("input[name='"+$t.attr("name")+"-value']").val() :$t.val();
				/**
				switch (type) {
				case "checkbox":
				case "radio":
					
					break;
				default:
					break;
				}
				**/
				var answer ={
						type :type,
						custom :custom,
						text:text,
						value:v
				}
				value.answers.push(answer);
			});
			
			values.push(value);
		}
		jq.val(JSON.stringify(values));
		return values;
		
	}
	
	function _setValue(jq,v){
		if(typeof v == "string" && v.length>0){
			v = $.parseJSON(v);
		}
		var values = v || [];
		var state = jq.data("surveyField");
		var options = state.options;
		var panel = state.panel;
		
		var questions = options.questions;
		
		for(i in  values){
			var value = values[i];
			var questionId = value.question;
			var answers = value.answers;
			
			panel.find("input[name='question-"+questionId+"-answer']").each(function(index){
				var $t = $(this);
				for(k in answers){
					var answer = answers[k];
					if(!answer.custom && answer.value==$t.val()){
						$t.attr("checked",'true');
						break;
					}
					if(answer.custom && $t.data("custom") && answer.text==$t.data("text")){
						$t.attr("checked",'true');
						$t.parent().find("input[name='"+$t.attr("name")+"-value']").val(answer.value);
						break;
					}
				}
			});
		}
	}
	
	$.fn.obpmSurveyField = function(options, param){
		
		if(typeof options=="string"){
			return $.fn.obpmSurveyField.method[options](this,param);
		}
		options = options || {};
		return this.each(function(){
			var t = $(this);
			var state = t.data("surveyField");
			if(state){
				$.extend(state.options,options);
			}else{
				var r =_init(t);
				state = t.data('surveyField', {
					options: $.extend({}, $.fn.obpmSurveyField.defaults, _parseOptions(t), options),
					panel: r
				});
				_bindEvents(t);
				_setValue(t,t.val());
			}
		});
	},
	
	$.fn.obpmSurveyField.defaults = {
			id:'',
			name:undefined,
			displayType:2,
			discription:'',
			questions:[]
	},
	
	$.fn.obpmSurveyField.method= {
			
		setValue:function(jq,value){
			
		},
		getValue:function(jq){
			jq.each(function(){
				_getValue($(this));
			});
		}
	}
	
})(jQuery);
; 
/* jQuery.qrcode 0.12.0 - http://larsjung.de/jquery-qrcode/ - uses //github.com/kazuhikoarase/qrcode-generator (MIT) */
!function(r){"use strict";function t(t,e,n,o){function i(r,t){return r-=o,t-=o,0>r||r>=u||0>t||t>=u?!1:a.isDark(r,t)}var a=r(n,e);a.addData(t),a.make(),o=o||0;var u=a.getModuleCount(),f=a.getModuleCount()+2*o,c=function(r,t,e,n){var o=this.isDark,i=1/f;this.isDark=function(a,u){var f=u*i,c=a*i,l=f+i,g=c+i;return o(a,u)&&(r>l||f>e||t>g||c>n)}};this.text=t,this.level=e,this.version=n,this.moduleCount=f,this.isDark=i,this.addBlank=c}function e(r,e,n,o,i){n=Math.max(1,n||1),o=Math.min(40,o||40);for(var a=n;o>=a;a+=1)try{return new t(r,e,a,i)}catch(u){}}function n(r,t,e){var n=e.size,o="bold "+e.mSize*n+"px "+e.fontname,i=w("<canvas/>")[0].getContext("2d");i.font=o;var a=i.measureText(e.label).width,u=e.mSize,f=a/n,c=(1-f)*e.mPosX,l=(1-u)*e.mPosY,g=c+f,s=l+u,h=.01;1===e.mode?r.addBlank(0,l-h,n,s+h):r.addBlank(c-h,l-h,g+h,s+h),t.fillStyle=e.fontcolor,t.font=o,t.fillText(e.label,c*n,l*n+.75*e.mSize*n)}function o(r,t,e){var n=e.size,o=e.image.naturalWidth||1,i=e.image.naturalHeight||1,a=e.mSize,u=a*o/i,f=(1-u)*e.mPosX,c=(1-a)*e.mPosY,l=f+u,g=c+a,s=.01;3===e.mode?r.addBlank(0,c-s,n,g+s):r.addBlank(f-s,c-s,l+s,g+s),t.drawImage(e.image,f*n,c*n,u*n,a*n)}function i(r,t,e){w(e.background).is("img")?t.drawImage(e.background,0,0,e.size,e.size):e.background&&(t.fillStyle=e.background,t.fillRect(e.left,e.top,e.size,e.size));var i=e.mode;1===i||2===i?n(r,t,e):(3===i||4===i)&&o(r,t,e)}function a(r,t,e,n,o,i,a,u){r.isDark(a,u)&&t.rect(n,o,i,i)}function u(r,t,e,n,o,i,a,u,f,c){a?r.moveTo(t+i,e):r.moveTo(t,e),u?(r.lineTo(n-i,e),r.arcTo(n,e,n,o,i)):r.lineTo(n,e),f?(r.lineTo(n,o-i),r.arcTo(n,o,t,o,i)):r.lineTo(n,o),c?(r.lineTo(t+i,o),r.arcTo(t,o,t,e,i)):r.lineTo(t,o),a?(r.lineTo(t,e+i),r.arcTo(t,e,n,e,i)):r.lineTo(t,e)}function f(r,t,e,n,o,i,a,u,f,c){a&&(r.moveTo(t+i,e),r.lineTo(t,e),r.lineTo(t,e+i),r.arcTo(t,e,t+i,e,i)),u&&(r.moveTo(n-i,e),r.lineTo(n,e),r.lineTo(n,e+i),r.arcTo(n,e,n-i,e,i)),f&&(r.moveTo(n-i,o),r.lineTo(n,o),r.lineTo(n,o-i),r.arcTo(n,o,n-i,o,i)),c&&(r.moveTo(t+i,o),r.lineTo(t,o),r.lineTo(t,o-i),r.arcTo(t,o,t+i,o,i))}function c(r,t,e,n,o,i,a,c){var l=r.isDark,g=n+i,s=o+i,h=e.radius*i,v=a-1,d=a+1,w=c-1,m=c+1,p=l(a,c),y=l(v,w),T=l(v,c),B=l(v,m),A=l(a,m),E=l(d,m),k=l(d,c),M=l(d,w),C=l(a,w);p?u(t,n,o,g,s,h,!T&&!C,!T&&!A,!k&&!A,!k&&!C):f(t,n,o,g,s,h,T&&C&&y,T&&A&&B,k&&A&&E,k&&C&&M)}function l(r,t,e){var n,o,i=r.moduleCount,u=e.size/i,f=a;for(p&&e.radius>0&&e.radius<=.5&&(f=c),t.beginPath(),n=0;i>n;n+=1)for(o=0;i>o;o+=1){var l=e.left+o*u,g=e.top+n*u,s=u;f(r,t,e,l,g,s,n,o)}if(w(e.fill).is("img")){t.strokeStyle="rgba(0,0,0,0.5)",t.lineWidth=2,t.stroke();var h=t.globalCompositeOperation;t.globalCompositeOperation="destination-out",t.fill(),t.globalCompositeOperation=h,t.clip(),t.drawImage(e.fill,0,0,e.size,e.size),t.restore()}else t.fillStyle=e.fill,t.fill()}function g(r,t){var n=e(t.text,t.ecLevel,t.minVersion,t.maxVersion,t.quiet);if(!n)return null;var o=w(r).data("qrcode",n),a=o[0].getContext("2d");return i(n,a,t),l(n,a,t),o}function s(r){var t=w("<canvas/>").attr("width",r.size).attr("height",r.size);return g(t,r)}function h(r){return w("<img/>").attr("src",s(r)[0].toDataURL("image/png"))}function v(r){var t=e(r.text,r.ecLevel,r.minVersion,r.maxVersion,r.quiet);if(!t)return null;var n,o,i=r.size,a=r.background,u=Math.floor,f=t.moduleCount,c=u(i/f),l=u(.5*(i-c*f)),g={position:"relative",left:0,top:0,padding:0,margin:0,width:i,height:i},s={position:"absolute",padding:0,margin:0,width:c,height:c,"background-color":r.fill},h=w("<div/>").data("qrcode",t).css(g);for(a&&h.css("background-color",a),n=0;f>n;n+=1)for(o=0;f>o;o+=1)t.isDark(n,o)&&w("<div/>").css(s).css({left:l+o*c,top:l+n*c}).appendTo(h);return h}function d(r){return m&&"canvas"===r.render?s(r):m&&"image"===r.render?h(r):v(r)}var w=jQuery,m=function(){var r=document.createElement("canvas");return Boolean(r.getContext&&r.getContext("2d"))}(),p="[object Opera]"!==Object.prototype.toString.call(window.opera),y={render:"canvas",minVersion:1,maxVersion:40,ecLevel:"L",left:0,top:0,size:200,fill:"#000",background:null,text:"no text",radius:0,quiet:0,mode:0,mSize:.1,mPosX:.5,mPosY:.5,label:"no label",fontname:"sans",fontcolor:"#000",image:null};w.fn.qrcode=function(r){var t=w.extend({},y,r);return this.each(function(){"canvas"===this.nodeName.toLowerCase()?g(this,t):w(this).append(d(t))})}}(function(){var r=function(){function r(t,e){if("undefined"==typeof t.length)throw new Error(t.length+"/"+e);var n=function(){for(var r=0;r<t.length&&0==t[r];)r+=1;for(var n=new Array(t.length-r+e),o=0;o<t.length-r;o+=1)n[o]=t[o+r];return n}(),o={};return o.getAt=function(r){return n[r]},o.getLength=function(){return n.length},o.multiply=function(t){for(var e=new Array(o.getLength()+t.getLength()-1),n=0;n<o.getLength();n+=1)for(var i=0;i<t.getLength();i+=1)e[n+i]^=a.gexp(a.glog(o.getAt(n))+a.glog(t.getAt(i)));return r(e,0)},o.mod=function(t){if(o.getLength()-t.getLength()<0)return o;for(var e=a.glog(o.getAt(0))-a.glog(t.getAt(0)),n=new Array(o.getLength()),i=0;i<o.getLength();i+=1)n[i]=o.getAt(i);for(var i=0;i<t.getLength();i+=1)n[i]^=a.gexp(a.glog(t.getAt(i))+e);return r(n,0).mod(t)},o}var t=function(t,e){var o=236,a=17,l=t,g=n[e],s=null,h=0,d=null,w=new Array,m={},p=function(r,t){h=4*l+17,s=function(r){for(var t=new Array(r),e=0;r>e;e+=1){t[e]=new Array(r);for(var n=0;r>n;n+=1)t[e][n]=null}return t}(h),y(0,0),y(h-7,0),y(0,h-7),A(),B(),k(r,t),l>=7&&E(r),null==d&&(d=D(l,g,w)),M(d,t)},y=function(r,t){for(var e=-1;7>=e;e+=1)if(!(-1>=r+e||r+e>=h))for(var n=-1;7>=n;n+=1)-1>=t+n||t+n>=h||(e>=0&&6>=e&&(0==n||6==n)||n>=0&&6>=n&&(0==e||6==e)||e>=2&&4>=e&&n>=2&&4>=n?s[r+e][t+n]=!0:s[r+e][t+n]=!1)},T=function(){for(var r=0,t=0,e=0;8>e;e+=1){p(!0,e);var n=i.getLostPoint(m);(0==e||r>n)&&(r=n,t=e)}return t},B=function(){for(var r=8;h-8>r;r+=1)null==s[r][6]&&(s[r][6]=r%2==0);for(var t=8;h-8>t;t+=1)null==s[6][t]&&(s[6][t]=t%2==0)},A=function(){for(var r=i.getPatternPosition(l),t=0;t<r.length;t+=1)for(var e=0;e<r.length;e+=1){var n=r[t],o=r[e];if(null==s[n][o])for(var a=-2;2>=a;a+=1)for(var u=-2;2>=u;u+=1)-2==a||2==a||-2==u||2==u||0==a&&0==u?s[n+a][o+u]=!0:s[n+a][o+u]=!1}},E=function(r){for(var t=i.getBCHTypeNumber(l),e=0;18>e;e+=1){var n=!r&&1==(t>>e&1);s[Math.floor(e/3)][e%3+h-8-3]=n}for(var e=0;18>e;e+=1){var n=!r&&1==(t>>e&1);s[e%3+h-8-3][Math.floor(e/3)]=n}},k=function(r,t){for(var e=g<<3|t,n=i.getBCHTypeInfo(e),o=0;15>o;o+=1){var a=!r&&1==(n>>o&1);6>o?s[o][8]=a:8>o?s[o+1][8]=a:s[h-15+o][8]=a}for(var o=0;15>o;o+=1){var a=!r&&1==(n>>o&1);8>o?s[8][h-o-1]=a:9>o?s[8][15-o-1+1]=a:s[8][15-o-1]=a}s[h-8][8]=!r},M=function(r,t){for(var e=-1,n=h-1,o=7,a=0,u=i.getMaskFunction(t),f=h-1;f>0;f-=2)for(6==f&&(f-=1);;){for(var c=0;2>c;c+=1)if(null==s[n][f-c]){var l=!1;a<r.length&&(l=1==(r[a]>>>o&1));var g=u(n,f-c);g&&(l=!l),s[n][f-c]=l,o-=1,-1==o&&(a+=1,o=7)}if(n+=e,0>n||n>=h){n-=e,e=-e;break}}},C=function(t,e){for(var n=0,o=0,a=0,u=new Array(e.length),f=new Array(e.length),c=0;c<e.length;c+=1){var l=e[c].dataCount,g=e[c].totalCount-l;o=Math.max(o,l),a=Math.max(a,g),u[c]=new Array(l);for(var s=0;s<u[c].length;s+=1)u[c][s]=255&t.getBuffer()[s+n];n+=l;var h=i.getErrorCorrectPolynomial(g),v=r(u[c],h.getLength()-1),d=v.mod(h);f[c]=new Array(h.getLength()-1);for(var s=0;s<f[c].length;s+=1){var w=s+d.getLength()-f[c].length;f[c][s]=w>=0?d.getAt(w):0}}for(var m=0,s=0;s<e.length;s+=1)m+=e[s].totalCount;for(var p=new Array(m),y=0,s=0;o>s;s+=1)for(var c=0;c<e.length;c+=1)s<u[c].length&&(p[y]=u[c][s],y+=1);for(var s=0;a>s;s+=1)for(var c=0;c<e.length;c+=1)s<f[c].length&&(p[y]=f[c][s],y+=1);return p},D=function(r,t,e){for(var n=u.getRSBlocks(r,t),c=f(),l=0;l<e.length;l+=1){var g=e[l];c.put(g.getMode(),4),c.put(g.getLength(),i.getLengthInBits(g.getMode(),r)),g.write(c)}for(var s=0,l=0;l<n.length;l+=1)s+=n[l].dataCount;if(c.getLengthInBits()>8*s)throw new Error("code length overflow. ("+c.getLengthInBits()+">"+8*s+")");for(c.getLengthInBits()+4<=8*s&&c.put(0,4);c.getLengthInBits()%8!=0;)c.putBit(!1);for(;;){if(c.getLengthInBits()>=8*s)break;if(c.put(o,8),c.getLengthInBits()>=8*s)break;c.put(a,8)}return C(c,n)};return m.addData=function(r){var t=c(r);w.push(t),d=null},m.isDark=function(r,t){if(0>r||r>=h||0>t||t>=h)throw new Error(r+","+t);return s[r][t]},m.getModuleCount=function(){return h},m.make=function(){p(!1,T())},m.createTableTag=function(r,t){r=r||2,t="undefined"==typeof t?4*r:t;var e="";e+='<table style="',e+=" border-width: 0px; border-style: none;",e+=" border-collapse: collapse;",e+=" padding: 0px; margin: "+t+"px;",e+='">',e+="<tbody>";for(var n=0;n<m.getModuleCount();n+=1){e+="<tr>";for(var o=0;o<m.getModuleCount();o+=1)e+='<td style="',e+=" border-width: 0px; border-style: none;",e+=" border-collapse: collapse;",e+=" padding: 0px; margin: 0px;",e+=" width: "+r+"px;",e+=" height: "+r+"px;",e+=" background-color: ",e+=m.isDark(n,o)?"#000000":"#ffffff",e+=";",e+='"/>';e+="</tr>"}return e+="</tbody>",e+="</table>"},m.createImgTag=function(r,t){r=r||2,t="undefined"==typeof t?4*r:t;var e=m.getModuleCount()*r+2*t,n=t,o=e-t;return v(e,e,function(t,e){if(t>=n&&o>t&&e>=n&&o>e){var i=Math.floor((t-n)/r),a=Math.floor((e-n)/r);return m.isDark(a,i)?0:1}return 1})},m};t.stringToBytes=function(r){for(var t=new Array,e=0;e<r.length;e+=1){var n=r.charCodeAt(e);t.push(255&n)}return t},t.createStringToBytes=function(r,t){var e=function(){for(var e=s(r),n=function(){var r=e.read();if(-1==r)throw new Error;return r},o=0,i={};;){var a=e.read();if(-1==a)break;var u=n(),f=n(),c=n(),l=String.fromCharCode(a<<8|u),g=f<<8|c;i[l]=g,o+=1}if(o!=t)throw new Error(o+" != "+t);return i}(),n="?".charCodeAt(0);return function(r){for(var t=new Array,o=0;o<r.length;o+=1){var i=r.charCodeAt(o);if(128>i)t.push(i);else{var a=e[r.charAt(o)];"number"==typeof a?(255&a)==a?t.push(a):(t.push(a>>>8),t.push(255&a)):t.push(n)}}return t}};var e={MODE_NUMBER:1,MODE_ALPHA_NUM:2,MODE_8BIT_BYTE:4,MODE_KANJI:8},n={L:1,M:0,Q:3,H:2},o={PATTERN000:0,PATTERN001:1,PATTERN010:2,PATTERN011:3,PATTERN100:4,PATTERN101:5,PATTERN110:6,PATTERN111:7},i=function(){var t=[[],[6,18],[6,22],[6,26],[6,30],[6,34],[6,22,38],[6,24,42],[6,26,46],[6,28,50],[6,30,54],[6,32,58],[6,34,62],[6,26,46,66],[6,26,48,70],[6,26,50,74],[6,30,54,78],[6,30,56,82],[6,30,58,86],[6,34,62,90],[6,28,50,72,94],[6,26,50,74,98],[6,30,54,78,102],[6,28,54,80,106],[6,32,58,84,110],[6,30,58,86,114],[6,34,62,90,118],[6,26,50,74,98,122],[6,30,54,78,102,126],[6,26,52,78,104,130],[6,30,56,82,108,134],[6,34,60,86,112,138],[6,30,58,86,114,142],[6,34,62,90,118,146],[6,30,54,78,102,126,150],[6,24,50,76,102,128,154],[6,28,54,80,106,132,158],[6,32,58,84,110,136,162],[6,26,54,82,110,138,166],[6,30,58,86,114,142,170]],n=1335,i=7973,u=21522,f={},c=function(r){for(var t=0;0!=r;)t+=1,r>>>=1;return t};return f.getBCHTypeInfo=function(r){for(var t=r<<10;c(t)-c(n)>=0;)t^=n<<c(t)-c(n);return(r<<10|t)^u},f.getBCHTypeNumber=function(r){for(var t=r<<12;c(t)-c(i)>=0;)t^=i<<c(t)-c(i);return r<<12|t},f.getPatternPosition=function(r){return t[r-1]},f.getMaskFunction=function(r){switch(r){case o.PATTERN000:return function(r,t){return(r+t)%2==0};case o.PATTERN001:return function(r,t){return r%2==0};case o.PATTERN010:return function(r,t){return t%3==0};case o.PATTERN011:return function(r,t){return(r+t)%3==0};case o.PATTERN100:return function(r,t){return(Math.floor(r/2)+Math.floor(t/3))%2==0};case o.PATTERN101:return function(r,t){return r*t%2+r*t%3==0};case o.PATTERN110:return function(r,t){return(r*t%2+r*t%3)%2==0};case o.PATTERN111:return function(r,t){return(r*t%3+(r+t)%2)%2==0};default:throw new Error("bad maskPattern:"+r)}},f.getErrorCorrectPolynomial=function(t){for(var e=r([1],0),n=0;t>n;n+=1)e=e.multiply(r([1,a.gexp(n)],0));return e},f.getLengthInBits=function(r,t){if(t>=1&&10>t)switch(r){case e.MODE_NUMBER:return 10;case e.MODE_ALPHA_NUM:return 9;case e.MODE_8BIT_BYTE:return 8;case e.MODE_KANJI:return 8;default:throw new Error("mode:"+r)}else if(27>t)switch(r){case e.MODE_NUMBER:return 12;case e.MODE_ALPHA_NUM:return 11;case e.MODE_8BIT_BYTE:return 16;case e.MODE_KANJI:return 10;default:throw new Error("mode:"+r)}else{if(!(41>t))throw new Error("type:"+t);switch(r){case e.MODE_NUMBER:return 14;case e.MODE_ALPHA_NUM:return 13;case e.MODE_8BIT_BYTE:return 16;case e.MODE_KANJI:return 12;default:throw new Error("mode:"+r)}}},f.getLostPoint=function(r){for(var t=r.getModuleCount(),e=0,n=0;t>n;n+=1)for(var o=0;t>o;o+=1){for(var i=0,a=r.isDark(n,o),u=-1;1>=u;u+=1)if(!(0>n+u||n+u>=t))for(var f=-1;1>=f;f+=1)0>o+f||o+f>=t||(0!=u||0!=f)&&a==r.isDark(n+u,o+f)&&(i+=1);i>5&&(e+=3+i-5)}for(var n=0;t-1>n;n+=1)for(var o=0;t-1>o;o+=1){var c=0;r.isDark(n,o)&&(c+=1),r.isDark(n+1,o)&&(c+=1),r.isDark(n,o+1)&&(c+=1),r.isDark(n+1,o+1)&&(c+=1),(0==c||4==c)&&(e+=3)}for(var n=0;t>n;n+=1)for(var o=0;t-6>o;o+=1)r.isDark(n,o)&&!r.isDark(n,o+1)&&r.isDark(n,o+2)&&r.isDark(n,o+3)&&r.isDark(n,o+4)&&!r.isDark(n,o+5)&&r.isDark(n,o+6)&&(e+=40);for(var o=0;t>o;o+=1)for(var n=0;t-6>n;n+=1)r.isDark(n,o)&&!r.isDark(n+1,o)&&r.isDark(n+2,o)&&r.isDark(n+3,o)&&r.isDark(n+4,o)&&!r.isDark(n+5,o)&&r.isDark(n+6,o)&&(e+=40);for(var l=0,o=0;t>o;o+=1)for(var n=0;t>n;n+=1)r.isDark(n,o)&&(l+=1);var g=Math.abs(100*l/t/t-50)/5;return e+=10*g},f}(),a=function(){for(var r=new Array(256),t=new Array(256),e=0;8>e;e+=1)r[e]=1<<e;for(var e=8;256>e;e+=1)r[e]=r[e-4]^r[e-5]^r[e-6]^r[e-8];for(var e=0;255>e;e+=1)t[r[e]]=e;var n={};return n.glog=function(r){if(1>r)throw new Error("glog("+r+")");return t[r]},n.gexp=function(t){for(;0>t;)t+=255;for(;t>=256;)t-=255;return r[t]},n}(),u=function(){var r=[[1,26,19],[1,26,16],[1,26,13],[1,26,9],[1,44,34],[1,44,28],[1,44,22],[1,44,16],[1,70,55],[1,70,44],[2,35,17],[2,35,13],[1,100,80],[2,50,32],[2,50,24],[4,25,9],[1,134,108],[2,67,43],[2,33,15,2,34,16],[2,33,11,2,34,12],[2,86,68],[4,43,27],[4,43,19],[4,43,15],[2,98,78],[4,49,31],[2,32,14,4,33,15],[4,39,13,1,40,14],[2,121,97],[2,60,38,2,61,39],[4,40,18,2,41,19],[4,40,14,2,41,15],[2,146,116],[3,58,36,2,59,37],[4,36,16,4,37,17],[4,36,12,4,37,13],[2,86,68,2,87,69],[4,69,43,1,70,44],[6,43,19,2,44,20],[6,43,15,2,44,16],[4,101,81],[1,80,50,4,81,51],[4,50,22,4,51,23],[3,36,12,8,37,13],[2,116,92,2,117,93],[6,58,36,2,59,37],[4,46,20,6,47,21],[7,42,14,4,43,15],[4,133,107],[8,59,37,1,60,38],[8,44,20,4,45,21],[12,33,11,4,34,12],[3,145,115,1,146,116],[4,64,40,5,65,41],[11,36,16,5,37,17],[11,36,12,5,37,13],[5,109,87,1,110,88],[5,65,41,5,66,42],[5,54,24,7,55,25],[11,36,12,7,37,13],[5,122,98,1,123,99],[7,73,45,3,74,46],[15,43,19,2,44,20],[3,45,15,13,46,16],[1,135,107,5,136,108],[10,74,46,1,75,47],[1,50,22,15,51,23],[2,42,14,17,43,15],[5,150,120,1,151,121],[9,69,43,4,70,44],[17,50,22,1,51,23],[2,42,14,19,43,15],[3,141,113,4,142,114],[3,70,44,11,71,45],[17,47,21,4,48,22],[9,39,13,16,40,14],[3,135,107,5,136,108],[3,67,41,13,68,42],[15,54,24,5,55,25],[15,43,15,10,44,16],[4,144,116,4,145,117],[17,68,42],[17,50,22,6,51,23],[19,46,16,6,47,17],[2,139,111,7,140,112],[17,74,46],[7,54,24,16,55,25],[34,37,13],[4,151,121,5,152,122],[4,75,47,14,76,48],[11,54,24,14,55,25],[16,45,15,14,46,16],[6,147,117,4,148,118],[6,73,45,14,74,46],[11,54,24,16,55,25],[30,46,16,2,47,17],[8,132,106,4,133,107],[8,75,47,13,76,48],[7,54,24,22,55,25],[22,45,15,13,46,16],[10,142,114,2,143,115],[19,74,46,4,75,47],[28,50,22,6,51,23],[33,46,16,4,47,17],[8,152,122,4,153,123],[22,73,45,3,74,46],[8,53,23,26,54,24],[12,45,15,28,46,16],[3,147,117,10,148,118],[3,73,45,23,74,46],[4,54,24,31,55,25],[11,45,15,31,46,16],[7,146,116,7,147,117],[21,73,45,7,74,46],[1,53,23,37,54,24],[19,45,15,26,46,16],[5,145,115,10,146,116],[19,75,47,10,76,48],[15,54,24,25,55,25],[23,45,15,25,46,16],[13,145,115,3,146,116],[2,74,46,29,75,47],[42,54,24,1,55,25],[23,45,15,28,46,16],[17,145,115],[10,74,46,23,75,47],[10,54,24,35,55,25],[19,45,15,35,46,16],[17,145,115,1,146,116],[14,74,46,21,75,47],[29,54,24,19,55,25],[11,45,15,46,46,16],[13,145,115,6,146,116],[14,74,46,23,75,47],[44,54,24,7,55,25],[59,46,16,1,47,17],[12,151,121,7,152,122],[12,75,47,26,76,48],[39,54,24,14,55,25],[22,45,15,41,46,16],[6,151,121,14,152,122],[6,75,47,34,76,48],[46,54,24,10,55,25],[2,45,15,64,46,16],[17,152,122,4,153,123],[29,74,46,14,75,47],[49,54,24,10,55,25],[24,45,15,46,46,16],[4,152,122,18,153,123],[13,74,46,32,75,47],[48,54,24,14,55,25],[42,45,15,32,46,16],[20,147,117,4,148,118],[40,75,47,7,76,48],[43,54,24,22,55,25],[10,45,15,67,46,16],[19,148,118,6,149,119],[18,75,47,31,76,48],[34,54,24,34,55,25],[20,45,15,61,46,16]],t=function(r,t){var e={};return e.totalCount=r,e.dataCount=t,e},e={},o=function(t,e){switch(e){case n.L:return r[4*(t-1)+0];case n.M:return r[4*(t-1)+1];case n.Q:return r[4*(t-1)+2];case n.H:return r[4*(t-1)+3];default:return void 0}};return e.getRSBlocks=function(r,e){var n=o(r,e);if("undefined"==typeof n)throw new Error("bad rs block @ typeNumber:"+r+"/errorCorrectLevel:"+e);for(var i=n.length/3,a=new Array,u=0;i>u;u+=1)for(var f=n[3*u+0],c=n[3*u+1],l=n[3*u+2],g=0;f>g;g+=1)a.push(t(c,l));return a},e}(),f=function(){var r=new Array,t=0,e={};return e.getBuffer=function(){return r},e.getAt=function(t){var e=Math.floor(t/8);return 1==(r[e]>>>7-t%8&1)},e.put=function(r,t){for(var n=0;t>n;n+=1)e.putBit(1==(r>>>t-n-1&1))},e.getLengthInBits=function(){return t},e.putBit=function(e){var n=Math.floor(t/8);r.length<=n&&r.push(0),e&&(r[n]|=128>>>t%8),t+=1},e},c=function(r){var n=e.MODE_8BIT_BYTE,o=t.stringToBytes(r),i={};return i.getMode=function(){return n},i.getLength=function(r){return o.length},i.write=function(r){for(var t=0;t<o.length;t+=1)r.put(o[t],8)},i},l=function(){var r=new Array,t={};return t.writeByte=function(t){r.push(255&t)},t.writeShort=function(r){t.writeByte(r),t.writeByte(r>>>8)},t.writeBytes=function(r,e,n){e=e||0,n=n||r.length;for(var o=0;n>o;o+=1)t.writeByte(r[o+e])},t.writeString=function(r){for(var e=0;e<r.length;e+=1)t.writeByte(r.charCodeAt(e))},t.toByteArray=function(){return r},t.toString=function(){var t="";t+="[";for(var e=0;e<r.length;e+=1)e>0&&(t+=","),t+=r[e];return t+="]"},t},g=function(){var r=0,t=0,e=0,n="",o={},i=function(r){n+=String.fromCharCode(a(63&r))},a=function(r){if(0>r);else{if(26>r)return 65+r;if(52>r)return 97+(r-26);if(62>r)return 48+(r-52);if(62==r)return 43;if(63==r)return 47}throw new Error("n:"+r)};return o.writeByte=function(n){for(r=r<<8|255&n,t+=8,e+=1;t>=6;)i(r>>>t-6),t-=6},o.flush=function(){if(t>0&&(i(r<<6-t),r=0,t=0),e%3!=0)for(var o=3-e%3,a=0;o>a;a+=1)n+="="},o.toString=function(){return n},o},s=function(r){var t=r,e=0,n=0,o=0,i={};i.read=function(){for(;8>o;){if(e>=t.length){if(0==o)return-1;throw new Error("unexpected end of file./"+o)}var r=t.charAt(e);if(e+=1,"="==r)return o=0,-1;r.match(/^\s$/)||(n=n<<6|a(r.charCodeAt(0)),o+=6)}var i=n>>>o-8&255;return o-=8,i};var a=function(r){if(r>=65&&90>=r)return r-65;if(r>=97&&122>=r)return r-97+26;if(r>=48&&57>=r)return r-48+52;if(43==r)return 62;if(47==r)return 63;throw new Error("c:"+r)};return i},h=function(r,t){var e=r,n=t,o=new Array(r*t),i={};i.setPixel=function(r,t,n){o[t*e+r]=n},i.write=function(r){r.writeString("GIF87a"),r.writeShort(e),r.writeShort(n),r.writeByte(128),r.writeByte(0),r.writeByte(0),r.writeByte(0),r.writeByte(0),r.writeByte(0),r.writeByte(255),r.writeByte(255),r.writeByte(255),r.writeString(","),r.writeShort(0),r.writeShort(0),r.writeShort(e),r.writeShort(n),r.writeByte(0);var t=2,o=u(t);r.writeByte(t);for(var i=0;o.length-i>255;)r.writeByte(255),r.writeBytes(o,i,255),i+=255;r.writeByte(o.length-i),r.writeBytes(o,i,o.length-i),r.writeByte(0),r.writeString(";")};var a=function(r){var t=r,e=0,n=0,o={};return o.write=function(r,o){if(r>>>o!=0)throw new Error("length over");for(;e+o>=8;)t.writeByte(255&(r<<e|n)),o-=8-e,r>>>=8-e,n=0,e=0;n=r<<e|n,e+=o},o.flush=function(){e>0&&t.writeByte(n)},o},u=function(r){for(var t=1<<r,e=(1<<r)+1,n=r+1,i=f(),u=0;t>u;u+=1)i.add(String.fromCharCode(u));i.add(String.fromCharCode(t)),i.add(String.fromCharCode(e));var c=l(),g=a(c);g.write(t,n);var s=0,h=String.fromCharCode(o[s]);for(s+=1;s<o.length;){var v=String.fromCharCode(o[s]);s+=1,i.contains(h+v)?h+=v:(g.write(i.indexOf(h),n),i.size()<4095&&(i.size()==1<<n&&(n+=1),i.add(h+v)),h=v)}return g.write(i.indexOf(h),n),g.write(e,n),g.flush(),c.toByteArray()},f=function(){var r={},t=0,e={};return e.add=function(n){if(e.contains(n))throw new Error("dup key:"+n);r[n]=t,t+=1},e.size=function(){return t},e.indexOf=function(t){return r[t]},e.contains=function(t){return"undefined"!=typeof r[t]},e};return i},v=function(r,t,e,n){for(var o=h(r,t),i=0;t>i;i+=1)for(var a=0;r>a;a+=1)o.setPixel(a,i,e(a,i));var u=l();o.write(u);for(var f=g(),c=u.toByteArray(),s=0;s<c.length;s+=1)f.writeByte(c[s]);f.flush();var v="";return v+="<img",v+=' src="',v+="data:image/gif;base64,",v+=f,v+='"',v+=' width="',v+=r,v+='"',v+=' height="',v+=t,v+='"',n&&(v+=' alt="',v+=n,v+='"'),v+="/>"};return t}();return function(r){"function"==typeof define&&define.amd?define([],r):"object"==typeof exports&&(module.exports=r())}(function(){return r}),!function(r){r.stringToBytes=function(r){function t(r){for(var t=[],e=0;e<r.length;e++){var n=r.charCodeAt(e);128>n?t.push(n):2048>n?t.push(192|n>>6,128|63&n):55296>n||n>=57344?t.push(224|n>>12,128|n>>6&63,128|63&n):(e++,n=65536+((1023&n)<<10|1023&r.charCodeAt(e)),t.push(240|n>>18,128|n>>12&63,128|n>>6&63,128|63&n))}return t}return t(r)}}(r),r}());; 
;
/**
 * 二维码控件
 * @author Happy
 * @param $
 */
(function($){
	
	/**
	 * 扫码结果处理类型
	 */
	var HandleType={
			HANDLE_TYPE_TEXT : "text",//显示值
			HANDLE_TYPE_LINK : "link",//打开链接
			HANDLE_TYPE_CALLBACK_EVENT : "callback_event"//执行扫码事件回调脚本
	};
	
	
	/**
	 * 控件初始化
	 */
	function _init(t,options){
		var panel = $('<div id="qrcode_container_'+options.id+'"></div>');
		
		var text = "";
		
		switch (options.handleType) {
		case HandleType.HANDLE_TYPE_TEXT:
			text = options.value;
			break;
		case HandleType.HANDLE_TYPE_LINK:
		case HandleType.HANDLE_TYPE_CALLBACK_EVENT:
			var formId = $("input[name='formid']").val();
			if($("input[name='_templateForm']").val().length>0){
				formId = $("input[name='_templateForm']").val();
			}
			var domainId = $("input[name='domain']").val();
			var docId = $("input[name='content.id']").val();
			var fieldId = options.fieldId;
			var applicationId = $("input[name='application']").val();
			var host = options.serverHost.length==0? window.location.protocol+"//"+window.location.host+contextPath : options.serverHost;
			text = host+"/portal/document/qrcodefield/ready?domainId="+domainId+"&formId="+formId+"&docId="+docId+"&fieldId="+fieldId+"&applicationId="+applicationId+"&_randomCode="+options.randomCode;
			break;
		default:
			break;
		}
		
		var opt = {
			    // render method: 'canvas', 'image' or 'div'
			    render: 'canvas',
			    // version range somewhere in 1 .. 40
			    minVersion: 1,
			    maxVersion: 40,
			    // error correction level: 'L', 'M', 'Q' or 'H'
			    ecLevel: 'L',
			    // offset in pixel if drawn onto existing canvas
			    left: 0,
			    top: 0,
			    // size in pixel
			    size: options.size,
			    // code color or image element
			    fill: '#000',
			    // background color or image element, null for transparent background
			    background: null,
			    // content
			    text: text,
			    // corner radius relative to module width: 0.0 .. 0.5
			    radius: 0,
			    // quiet zone in modules
			    quiet: 0,
			    // modes
			    // 0: normal
			    // 1: label strip
			    // 2: label box
			    // 3: image strip
			    // 4: image box
			    mode: 0,
			    mSize: 0.1,
			    mPosX: 0.5,
			    mPosY: 0.5,
			    label: 'no label',
			    fontname: 'sans',
			    fontcolor: '#000',
			    image: null
			};
		panel.insertAfter(t);
		panel.qrcode(opt);
		
		return panel;
	}
	
	
	/**
	 * 事件绑定
	 */
	function _bindEvents(t){
		var options = t.data("qrcode").options;
		if(options.handleType==HandleType.HANDLE_TYPE_CALLBACK_EVENT){
			_refreshOnCallback(t);
		}
	}
	
	function _refreshOnCallback(t){
		var options = t.data("qrcode").options;
		var docId = $("input[name='content.id']").val();
		var tid = window.setInterval(function(){
			$.get(contextPath + '/portal/document/qrcodefield/handle?action=track&_randomCode='+options.randomCode, function(data){
				  if(data=="success"){
					  window.clearInterval(tid);
					  Activity.refreshForm();
				  }
				});
		}, 5*1000);
	}
	
	/**
	 * 解析控件参数
	 */
	function _parseOptions(t){
		var options = {};
		options.id = t.attr("id");
		options.fieldId = t.data("fieldId");
		options.handleType= t.data("handleType");
		options.serverHost = t.data("serverHost");
		options.name = t.attr("name");
		options.discription = HTMLDencode(t.data("discription"))? HTMLDencode(t.data("discription")):options.name;
		options.value = t.val();
		options.refreshOnChanged = (t.data("refreshOnChanged") ? (t.data("refreshOnChanged") == 'true' || t.data("refreshOnChanged") == true) : undefined),
		options.displayType = t.data("displayType");
		options.size = t.data("size");
		
		var uuid = "";
		var len = 16;
		var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
		var maxPos = chars.length;
		for (i = 0;i<len;i++){
			uuid+=chars.charAt(Math.floor(Math.random()*maxPos));
		}
		options.randomCode = uuid;
		return options;
	}
	
	$.fn.obpmQRCodeField = function(options, param){
		
		if(typeof options=="string"){
			return $.fn.obpmQRCodeField.method[options](this,param);
		}
		options = options || {};
		return this.each(function(){
			var t = $(this);
			var state = t.data("qrcode");
			if(state){
				$.extend(state.options,options);
			}else{
				options = $.extend({}, $.fn.obpmQRCodeField.defaults, _parseOptions(t), options);
				var r =_init(t,options);
				state = t.data('qrcode', {
					options: options,
					panel: r
				});
				_bindEvents(t);
			}
		});
	},
	
	/**
	 * 定义二维码控件默认值
	 */
	$.fn.obpmQRCodeField.defaults = {
			id:'',
			fieldId:'',
			handleType:HandleType.HANDLE_TYPE_TEXT,
			serverHost:'',
			name:'',
			value:'',
			refreshOnChanged:false,
			discription:'',
			displayType:null
	},
	/**
	 * 定义二维码控件方法
	 */
	$.fn.obpmQRCodeField.method= {
			
		setValue:function(jq,value){
			
		},
		getValue:function(jq){
			jq.each(function(){
				//nothing
			});
		}
	}
	
})(jQuery);
; 
/**
 * 操作按钮执行框架
 * <p>框架主要封装操作按钮的执行流程，和一些通用操作接口，在执行过程中，具体的行为实现由具体的按钮类型实例完成</p>
 * @author Happy
 */
var Activity ={
		
		_LOCK:false,//操作按钮触发锁，用于避免多次触发执行
		
		/**
		 * 触发按钮点击动作
		 * @param actId
		 * 		操作id
		 * @param actType
		 * 		操作类型代码
		 * @param params
		 * 		额外参数设置
		 */
		doExecute:function(actId,actType,params){
			var activityType = null;//操作类型实例
			switch (actType) {
			case 1://载入视图
				activityType = new QueryBtn(actId,params);
				break;
			case 2://新建
				activityType = new CreateBtn(actId,params);
				break;
			case 3://删除
				activityType = new DeleteBtn(actId,params);
				break;
			case 4://保存并启动流程
				activityType = new SaveStartWorkflow(actId,params);
				break;
			case 5://流程处理
				activityType = new WorkflowProcess(actId,params);
				break;
			case 8://关闭窗口
				activityType = new CloseWindowBtn(actId,params);
				break;
			case 9://保存并关闭窗口
				activityType = new SaveCloseWindowBtn(actId,params);
				break;
				case 10://返回
				activityType = new BackBtn(actId,params);
				break;
			case 11://保存并返回
				activityType = new SaveBackBtn(actId,params);
				break;
			case 13://无类型
				activityType = new NoneBtn(actId,params);
				break;
			case 14://html打印
				activityType = new HtmlPrintBtn(actId,params);
				break;
			case 16://导出excel
				activityType = new ExportToExcelBtn(actId,params);
				break;
			case 15://html打印(带流程历史)
				activityType = new HtmlPrintWithHisBtn(actId,params);
				break;
			case 18://清空数据
				activityType = new ClearAllBtn(actId,params);
				break;
			case 19://保存草稿（不校验）
				activityType = new SaveWithoutValidateBtn(actId,params);
				break;
			case 20://批量提交
				activityType = new BatchApproveBtn(actId,params);
				break;
			case 21://保存并复制
				activityType = new SaveCopyBtn(actId,params);
				break;
			case 25://导出pdf
				activityType = new ExportToPdfBtn(actId,params);
				break;
			case 26://文件下载
				activityType = new FileDownloadBtn(actId,params);
				break;
			case 27://文件下载
				activityType = new ExcelImportBtn(actId,params);
				break;
			case 28://电子签章
				activityType = new SignatureBtn(actId,params);
				break;
			case 29://批量电子签章
				activityType = new BatchSignatureBtn(actId,params);
				break;
			case 30://自定义打印（套打）
				activityType = new FlexPrintBtn(actId,params);
				break;
			case 33://启动流程
				activityType = new StartWorkflow(actId,params);
				break;
			case 34://保存
				activityType = new SaveBtn(actId,params);
				break;
			case 36://打印视图
				activityType = new PrintViewBtn(actId,params);
				break;
			case 37://通过手机邮件转发
				activityType = new TranspondBtn(actId,params);
				break;	
			case 42://保存并新建
				activityType = new SaveNewBtn(actId,params);
				break;
			case 43://跳转
				activityType = new JumpToBtn(actId,params);
				break;
			case 45://归档
				activityType = new ArchiveBtn(actId,params);
				break;
			default:
				
				break;
			}
			if(!activityType) return;
			
			Activity.doBefore(activityType);
			
		},
		
		/**
		 * 执行前操作
		 * @param activityType
		 * 		操作类型实例
		 * @returns {Boolean}
		 */
		doBefore : function(activityType){

			if(Activity._LOCK) return;
			
			Activity._LOCK = true;
			dy_lock();
			setTimeout(function(){
				//一分钟后自动释放锁
				Activity._LOCK = false;
				dy_unlock();
			}, 60*1000);
			
			if(activityType.doBefore){//按钮动作执行前的准备与校验操作
				if(!activityType.doBefore()) {
					Activity._LOCK = false;
					return;
				}
			}

			var flag = false;//执行完脚本后,是否进行下一步提交
			jQuery.ajax({
				type: 'POST',
				async:false, 
				url: contextPath + '/portal/dynaform/activity/runbeforeactionscript.action?_actid=' + activityType.actId,
				dataType : 'text',
				data: activityType.getBeforePostData(),
				success:function(result) {
					if(result != null && result != ""){
						result = result.replace(/\n/g,"<br/>");
						result = result.replace(/\r/g,"<br/>");
			        	var jsmessage = eval("(" + result + ")");
			        	var type = jsmessage.type;
			        	var content = jsmessage.content;
			        	
			        	if(type){
			        		if(type == '16'){
				        		//alert(content);
			        			Activity.showMessage(content,"error");
				        		flag = false;
				        	}
				        	
				        	if(type == '32'){
				        		var rtn = window.confirm(content);
				        		if(!rtn){
				        			flag = false;
				        		}else {
				        			flag = true;
				        		}
				        	}
			        	}else {
			        		flag = true;
			        	}
			        	
			        	if(flag){
			        		var changedField = jsmessage.changedField;
			        		if( typeof(changedField) != "undefined" && changedField != null && changedField.length > 0){
			        			for(var i=0; i<changedField.length; i++){
			        				var field = changedField[i];
				        			for(var key in field)	{
				        				if(document.getElementsByName(key).length>0){
				        					document.getElementsByName(key)[0].value = field[key];
				        				}
				        			}
			        			}
			        		}
			        	}
			        }else {
			        	flag = true;
			        }
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					Activity._LOCK = false;
					dy_unlock();
					if(XMLHttpRequest.readyState == 0){
						Activity.showMessage("网络已断开！","error");
						Activity.refreshForm();
					}else{
						Activity.showMessage(errorThrown.message,"error");
					}
				}
			});
			if(!flag){
				Activity._LOCK = false;
				dy_unlock();
			}else{
				Activity.doAction(activityType);
			}
		},
		/**
		 * 执行按钮操作
		 * @param activityType
		 * 		操作类型实例
		 */
		doAction:function(activityType){
			
			if(activityType.doAction){
				//按钮动作时执行的其他业务操作
				if(!activityType.doAction()) {
					Activity._LOCK = false;
					dy_unlock();
					return;
				}
			}
			
			jQuery.ajax({
				type: 'POST',
				async:true, 
				url: contextPath + '/portal/dynaform/activity/execute.action?_activityid=' + activityType.actId,
				dataType : 'json',
				data: activityType.getActionPostData(),
				success:function(result) {
					if(result.status==1){
						var backSwitch = false;	//保存并返回按钮是否执行doAfter--膏药，要移除
						if(result.message && result.message.length>3){
							Activity.showMessage(result.message,"info");
						}
						switch (result.data.resultType) {//结果处理类型
						case "form":
							Activity.refreshForm(activityType);
							break;
						case "view":
							Activity.refreshView(activityType);
							break;
						case "back":
							var backUrl = $("#_backURL").val();
							if(backUrl && backUrl.length>0){
								window.location.href = backUrl;
							}else{
								var opentype = $("input[name='openType']").val();
								if("277"==opentype || "16"==opentype){//弹出层打开
									Activity.closeWindow();
								}else{
									history.back();
								}
								
							}
							break;
						case "close":
							var backUrl = $("#_backURL").val();
							var opentype = $("input[name='openType']").val();
							if(!opentype || ""==opentype){
								if(backUrl && backUrl.length>0){
									window.location.href = backUrl;
								}
								return;
							}
							Activity.closeWindow();
							break;
						case "message":
							if(result.data.resultData.type==16){//alert
								Activity.showMessage(result.data.resultData.content,"error");
							}else if(result.data.resultData.type==32){//confirm
								
							}
							Activity._LOCK = false;
							if(activityType.actType == 11){	//--膏药，要移除
								backSwitch = true;
							}
							break;
						case "setParameter"://给activityType传参
							activityType.params.parameter = result.data.resultData;
							break;
						case "validate":
							var fieldErrors = result.data.resultData;
							var content = "";
							for(var name in fieldErrors){
								var fr = fieldErrors[name];
								for(var i in fr){
									var m = fr[i];
									content+=m+";";
								}
							}
							Activity.showMessage(content,"error");
							Activity._LOCK = false;
							dy_unlock();
							hideLoadingToast();
							return;
							break;
						case "exception":
							Activity.showMessage(result.data.resultData,"error",60*1000);
							return;
							break;

						default:
							break;
						}



						//按钮动作执行后的其他业务操作
						if(activityType.doAfter && typeof activityType.doAfter == "function"){
							if((activityType.actType != 11 || backSwitch)){	//--膏药，要移除
								activityType.doAfter(result.data);
							}
						}
					}else{
						Activity.showMessage(result.message,"error");
					}
					Activity._LOCK = false;
					dy_unlock();
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					Activity._LOCK = false;
					dy_unlock();
					if(XMLHttpRequest.readyState == 0){
						Activity.showMessage("网络已断开！","error");
					}else{
						Activity.showMessage(errorThrown.message,"error");
					}
				}
			});


		},
		
		/**
		 * 刷新表单
		 * @param activityType
		 * 		操作类型实例
		 */
		refreshForm:function(activityType){
			jQuery.ajax({
				type: 'POST',
				async:true, 
				url: contextPath + '/portal/dynaform/activity/refreshForm.action',
				dataType : 'json',
				data: jQuery("#document_content").serialize(),
				success:function(result) {
					if(result.status==1){
						var datas = result.data;
						var activityHtml = datas["activityHtml"];
						if(activityHtml){
							$(".formActBtn").html(activityHtml);
						}
						var formHtml = datas["formHtml"];
						if(formHtml){
							$("#_formHtml").html(formHtml)
						}
						//---
						dy_lock();//显示loading层
						//填充系统字段
						if(datas["systemFields"]){
							for(var n in datas["systemFields"]){
								$("input[name='"+n+"']").val(datas["systemFields"][n]);
							}
						}
						
						//渲染流程处理人列表、流程状态标签、流程历史按钮
						if(datas["processorHtml"]){
							$("#processorHtml").html('<div class="processor"><b>'+datas["processorHtml"]+'</b></div>');
						}
						if(datas["flowStateHtml"]){
							$("#flowStateHtml").html('<div class="flowstate" onmouseover="showFlowState(\'flowstate\');"><b>'+datas["flowStateHtml"]+'</b></div>');
						}
						if(jQuery("input[name='content.stateid']").val().length>0){
							jQuery(".flowbtn").show();
						}
						initFormCommon();//表单公用的初始化方法
						//渲染流程提交按钮
						if($("input[activityType='5']")){
							FlowPanel.refreshFlowPanel("init");
						}
//						adjustDocumentLayout4form();//调整相关文档布局
						dy_unlock();//隐藏loading层
						
						//---
						/**
						if(callback && typeof callback == "function"){
							callback(result.data);
						}
						**/
					}else{
						Activity.showMessage(result.message,"error");
						Activity._LOCK = false;
					}

				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.readyState == 0){
						Activity.showMessage("网络已断开！","error");
					}else{
						Activity.showMessage(errorThrown.message,"error");
					}
					Activity._LOCK = false;
				}
			});
		},
		
		/**
		 * 刷新视图
		 * @param activityType
		 * 		操作类型实例
		 */
		refreshView:function(activityType){
			jQuery.ajax({
				type: 'POST',
				async:true, 
				url: contextPath + '/portal/dynaform/activity/refreshView.action',
				dataType : 'json',
				data: jQuery(document.forms[0]).serialize(),
				success:function(result) {
					if(result.status==1){
						var datas = result.data;
						var activityHtml = datas["activityHtml"];
						if(activityHtml){
							//$(".formActBtn").html(activityHtml);
						}
						var viewHtml = datas["viewHtml"];
						if(viewHtml){
							$("#viewHtml").html(viewHtml)
						}
						//---
						dy_lock();//显示loading层
						initListComm();	//列表视图公用初始化方法
						adjustDataIteratorSize();
						setTimeout(function(){
							listViewAdjustLayout();
						},10);	//调整当前窗口布局
						dy_unlock();//隐藏loading层
						
						//---
						/**
						if(callback && typeof callback == "function"){
							callback(result.data);
						}
						**/
					}else{
						Activity.showMessage(result.message,"error");
						Activity._LOCK = false;
					}
					Activity._LOCK = false;
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.readyState == 0){
						Activity.showMessage("网络已断开！","error");
					}else{
						Activity.showMessage(errorThrown.message,"error");
					}
					Activity._LOCK = false;
				}
			});
		},
		/**
		 * 返回
		 * @param activityType
		 * 		操作类型实例
		 */
		back:function(activityType){
			var _time = 0;
			if(activityType.actType == 11 || activityType.actType == 13){
				_time = 3000;
			}
					
			setTimeout(function(){
				var backUrl = $("#_backURL").val();
				if(backUrl && backUrl.length>0){
					window.location.href = backUrl;
				}else{
					var opentype = $("input[name='openType']").val();
					if("277"==opentype || "16"==opentype){//弹出层打开
						Activity.closeWindow();
					}else{
						history.back();
					}
					
				}
			},_time);
		},
		/**
		 * 关闭窗口
		 */
		closeWindow:function() {
			
			if($("#myModalexample",parent.document).hasClass("active")){
				parent.MyPopup._modal.trigger("close");
				return;
			}
			
			
			var  view_id = $("input[name='view_id']").val();
			if(parent){
			    var sub_divid  =parent.document.getElementById('sub_divid');
				var doc_obj = parent.document.getElementById(view_id);
				if (doc_obj) { // 本区域返回
					doc_obj.src= sub_divid.value;
					parent.ev_reloadParent();
					return;
				}
				
				if(parent.frames['main_iframe']){
					var viewiFrame = parent.frames['main_iframe'].frames['detail'].frames[view_id];
					if(viewiFrame){
						try{
						 	viewiFrame.ev_reload()
						 	hidden();
						}catch(ex){}
					}else{
					  parent.close();
					  parent.parentWindow.ev_reload();
					}
				}else{
				  OBPM.dialog.doExit();
				}
			} else {
				OBPM.dialog.doExit();
			}
		},
		
	    /**
		 * 显示消息提示
		 * @param msg
		 * 		消息内容
		 * @param type
		 * 		消息类型（'info','error'）
		 * @param hideAfter
		 * 		延时几秒后关闭消息窗体
		 */
		showMessage : function(msg, type,hideAfter) {
	    	if(!msg) return;
	    	window.showMessage(type,msg);
	    	dy_unlock();
	    	
	    	/**
		    var type = "undefined" == typeof type ? "info": type,
		    		hideAfter = "undefined" == typeof hideAfter ? 3 : hideAfter;//默认3秒停留时间
		    $._messengerDefaults = {
		        extraClasses: "messenger-fixed messenger-on-top",
		        theme: 'flat',
		        messageDefaults: {
		            showCloseButton: true,
		            hideAfter: hideAfter
		        }
		    },
		    $.globalMessenger().post({
		        message: msg,
		        type: type
		    });
		    **/
		},
		makeAllFieldAble :function(elements) {
			var fields = [];
			if (!elements) {
				elements = document.forms[0].elements;
			}
			for (var i = 0; i < elements.length; i++) {
				var element = elements[i];
				if (element.disabled == true) {
					element.disabled = false;
					fields.push(element);
				}
			}
			
			return fields;
		},
		
		setFieldDisabled : function(fields){
			for (var i = 0; i < fields.length; i++) {
				var element = fields[i];
				if (element.disabled == false) {
					element.disabled = true;
				}
			}
			
		},
		
		cache:{
			
		}
		
		
}; 
/**
 * 保存并提交流程操作
 * @author Happy
 */
function SaveStartWorkflow(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 4;
	
	
	if(typeof SaveStartWorkflow._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SaveStartWorkflow.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		SaveStartWorkflow.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		SaveStartWorkflow.prototype.doBefore = function(){
			var flag = true;
			
			var oOperation = document.getElementById("operation");
			if (oOperation) {
				oOperation.value = "doSave";
			}
			
			setHTMLValue();//设置html控件值
			
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SaveStartWorkflow.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SaveStartWorkflow.prototype.doAfter = function(result){
		}
		
		SaveStartWorkflow._initialized = true;
	
	}
	
	
	
	return this;
}; 
var historyRecory = history.length;

/**
 * 流程处理
 * @author Happy
 */
function WorkflowProcess(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 5;
	
	
	if(typeof WorkflowProcess._initialized == "undefined"){
		
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		WorkflowProcess.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		WorkflowProcess.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		WorkflowProcess.prototype.doBefore = function(){
			showLoadingToast();
			setHTMLValue();
			var flag = true;
			return flag;
		}
		/**
		 * 按钮动作时执行的其他业务操作
		 */
		WorkflowProcess.prototype.doAction = function(){
			//doWordSave();
			return true;
		}
		
		/**
		 * 按钮动作执行后的其他业务操作
		 */
		WorkflowProcess.prototype.doAfter = function(result){
			hideLoadingToast();
			var backUrl = $("#_backURL").val();
			var openType = $("#openType").val();
			if($("#openType").val()=="from_weixin_message"){//从微信待办消息中打开
				if (typeof WeixinJSBridge == "undefined"){
				    if( document.addEventListener ){
				        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
				    }else if (document.attachEvent){
				        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
				        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
				    }
				}
				try {
					WeixinJSBridge.invoke('closeWindow',{},function(res){
						//alert('关闭微信页面');
					});
				} catch (e) {
				}
				
			}else if(backUrl && backUrl.length>0){
				setTimeout(function(){
					window.location.href = backUrl;
				}, 3000);
			}
			else if(GetReferrer()==""){//判断来路为空
				try {
					WeixinJSBridge.invoke('closeWindow',{},function(res){
						//alert('关闭微信页面');
					});
				} catch (e) {
					window.close();
				}
			}else if($("#myModalexample",parent.document).hasClass("active")){
				parent.MyPopup._modal.trigger("close");
				return;
			}
			else{
				var historyRecory2 = history.length;
				var backNum = -2;
				if(historyRecory2-historyRecory > 1){
					backNum = -(historyRecory2-historyRecory+1);
				}
				history.go(backNum);
			}
		}
	}
		
		WorkflowProcess._initialized = true;
	
	
	
	
	return this;
}

function GetReferrer() {//获取来路URL
	var ref = '';  
		if (document.referrer.length > 0) {  
			ref = document.referrer;  
		}  
	try {  
		if (ref.length == 0 && opener.location.href.length > 0) {  
			ref = opener.location.href;  
		}  
	} catch (e) {} 
	return ref;
}

; 
/**
 * 保存操作
 * 
 */
function SaveBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 34;
	
	
	if(typeof SaveBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SaveBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		SaveBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		SaveBtn.prototype.doBefore = function(){
			var flag = false;
			
			var oOperation = document.getElementById("operation");
			if (oOperation) {
				oOperation.value = "doSave";
			}
			setHTMLValue();//设置html控件值
			var isword = false;

			if (!isword || isword == 'false') {
				flag = true;
			} else {
				if (retvalue >= 0) { // 有返回值才保存
					flag = true;
				}
			}
			/**
			if(flag){
				flag = ifSubSaveForm();
			}
			**/
			
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SaveBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SaveBtn.prototype.doAfter = function(result){
			if(result.resultType === "message"){
				var versions = parseInt($("input[name='content.versions']").val());
				$("input[name='content.versions']").val((versions + 1) + "");
			}
		}
		
		SaveBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 保存并返回操作
 * 
 */
function SaveBackBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 11;
	
	
	if(typeof SaveBackBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SaveBackBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		SaveBackBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		SaveBackBtn.prototype.doBefore = function(){
			var flag = false;
			
			var oOperation = document.getElementById("operation");
			if (oOperation) {
				oOperation.value = "doSave";
			}
			setHTMLValue();//设置html控件值
			//var retvalue = doWordSave();
			// if(!retvalue) {
			// 	alert('Word文档已经被其他用户更新， 请刷新页面加载最新的Word文档！');
			// 	return false;
			// }
			var isword = false;

			if (!isword || isword == 'false') {
				flag = true;
			} else {
				if (retvalue >= 0) { // 有返回值才保存
					flag = true;
				}
			}
			// if(flag){
			// 	flag = ifSubSaveForm();
			// }
			// if(flag){
			// 	flag = beforeAct(this.actType);
			// }
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SaveBackBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SaveBackBtn.prototype.doAfter = function(result){
			var backUrl = $("#_backURL").val();
			var openType = $("#openType").val();
			if($("#openType").val()=="from_weixin_message"){//从微信待办消息中打开
				if (typeof WeixinJSBridge == "undefined"){
				    if( document.addEventListener ){
				        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
				    }else if (document.attachEvent){
				        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
				        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
				    }
				}
				try {
					WeixinJSBridge.invoke('closeWindow',{},function(res){
						//alert('关闭微信页面');
					});
				} catch (e) {
				}
				
			}else if(backUrl && backUrl.length>0){
				setTimeout(function(){
					window.location.href = backUrl;
				}, 3000);
			}
			else if(GetReferrer()==""){//判断来路为空
				try {
					WeixinJSBridge.invoke('closeWindow',{},function(res){
						//alert('关闭微信页面');
					});
				} catch (e) {
					window.close();
				}
			}else if($("#myModalexample",parent.document).hasClass("active")){
				parent.MyPopup._modal.trigger("close");
				return;
			}
			else{
				var historyRecory2 = history.length;
				var backNum = -1;
				if(historyRecory2-historyRecory > 1){
					backNum = -(historyRecory2-historyRecory+1);
				}
				
				setTimeout(function(){
					history.go(backNum);
				}, 3000);
			}
		}
		
		SaveBackBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 保存草稿(不校验)操作
 * 
 */
function SaveWithoutValidateBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 34;
	
	
	if(typeof SaveWithoutValidateBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SaveWithoutValidateBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			jQuery("iframe[name='display_view']").trigger("save");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		SaveWithoutValidateBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		SaveWithoutValidateBtn.prototype.doBefore = function(){
			var flag = false;
			
			var oOperation = document.getElementById("operation");
			if (oOperation) {
				oOperation.value = "doSave";
			}
			setHTMLValue();//设置html控件值
			var retvalue = doWordSave();
			if(!retvalue) {
				alert('Word文档已经被其他用户更新， 请刷新页面加载最新的Word文档！');
				return false;
			}
			var isword = false;

			if (!isword || isword == 'false') {
				flag = true;
			} else {
				if (retvalue >= 0) { // 有返回值才保存
					flag = true;
				}
			}
			if(flag){
				flag = ifSubSaveForm();
			}
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SaveWithoutValidateBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SaveWithoutValidateBtn.prototype.doAfter = function(result){
		}
		
		SaveWithoutValidateBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 保存并关闭窗口
 * 
 */
function SaveCloseWindowBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 9;
	
	
	if(typeof SaveCloseWindowBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SaveCloseWindowBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		SaveCloseWindowBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		SaveCloseWindowBtn.prototype.doBefore = function(){
			var flag = false;
			
			var oOperation = document.getElementById("operation");
			if (oOperation) {
				oOperation.value = "doSave";
			}
			setHTMLValue();//设置html控件值
			var retvalue = doWordSave();
			if(!retvalue) {
				alert('Word文档已经被其他用户更新， 请刷新页面加载最新的Word文档！');
				return false;
			}
			var isword = false;

			if (!isword || isword == 'false') {
				flag = true;
			} else {
				if (retvalue >= 0) { // 有返回值才保存
					flag = true;
				}
			}
			if(flag){
				flag = ifSubSaveForm();
			}
			if(flag){
				flag = beforeAct(this.actType);
			}
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SaveCloseWindowBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SaveCloseWindowBtn.prototype.doAfter = function(result){
		}
		
		SaveCloseWindowBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 保存并返回操作
 * 
 */
function SaveNewBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 10;
	this.withOld = params.withOld;//是否带旧数据
	
	
	if(typeof SaveNewBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SaveNewBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		SaveNewBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		SaveNewBtn.prototype.doBefore = function(){
			var flag = true;
			
			var oOperation = document.getElementById("operation");
			if (oOperation) {
				oOperation.value = "doSave";
			}
			setHTMLValue();//设置html控件值
			//flag = ifSubSaveForm();
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SaveNewBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SaveNewBtn.prototype.doAfter = function(result){
			
			jQuery.ajax({
				type: 'POST',
				async:true, 
				url: contextPath + '/portal/dynaform/activity/newDocument.action?_withOld='+this.withOld,
				dataType : 'json',
				timeout: 3000,
				data: jQuery("#document_content").serialize(),
				success:function(result) {
					if(result.status==1){
						var datas = result.data;
						var activityHtml = datas["activityHtml"];
						if(activityHtml){
							$(".formActBtn").html(activityHtml);
						}
						var formHtml = datas["formHtml"];
						if(formHtml){
							$("#_formHtml").html(formHtml)
						}
						dy_lock();//显示loading层
						//渲染流程提交按钮
						if($("input[activityType='5']")){
							FlowPanel.refreshFlowPanel("init");
						}
						//填充系统字段
						if(datas["systemFields"]){
							for(var n in datas["systemFields"]){
								$("input[name='"+n+"']").val(datas["systemFields"][n]);
							}
						}
						//渲染流程处理人列表、流程状态标签、流程历史按钮
						$("#processorHtml").html('');
						$("#flowStateHtml").html('');
						jQuery(".flowbtn").hide();
						initFormCommon();//表单公用的初始化方法
//						adjustDocumentLayout4form();//调整相关文档布局
						dy_unlock();//隐藏loading层
					}else{
						Activity.showMessage(result.message,"error");
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					if(XMLHttpRequest.readyState == 0){
						Activity.showMessage("网络已断开！","error");
					}else{
						Activity.showMessage(errorThrown.message,"error");
					}
				}
			});
			
		}
		
		SaveNewBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 保存并返回操作
 * 
 */
function BackBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 10;
	
	
	if(typeof BackBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		BackBtn.prototype.getBeforePostData = function(){
			return jQuery("#document_content").serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		BackBtn.prototype.getActionPostData = function(){
			return jQuery("#document_content").serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		BackBtn.prototype.doBefore = function(){
			var flag = false;
			
			flag = ifSubSaveForm();
			if(flag){
				flag = beforeAct(this.actType);
			}
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		BackBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		BackBtn.prototype.doAfter = function(result){
		}
		
		BackBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 无类型操作
 * 
 */
function NoneBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 13;
	this.afterActionType = params.afterActionType;
	this.afterActionDispatcherUrlScript = params.afterActionDispatcherUrlScript;
	
	if(typeof NoneBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		NoneBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		NoneBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		NoneBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		NoneBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		NoneBtn.prototype.doAfter = function(result){
			switch (this.afterActionType) {
			case 0:  //无
				break;
			case 1:  //返回
				Activity.back(this);
				break;
			case 2:   //关闭
				var opentype = $("input[name='openType']").val();
				if("277"==opentype || "16"==opentype){//弹出层打开
					Activity.closeWindow();
				}else{
					Activity.back(this);
				}
				break;
			case 3:   //跳转
				window.location.href = this.afterActionDispatcherUrlScript;
				break;
			default:
				break;
			}
		}
		
		NoneBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 保存并复制操作
 * 
 */
function SaveCopyBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 21;
	
	
	if(typeof SaveCopyBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SaveCopyBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		SaveCopyBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		SaveCopyBtn.prototype.doBefore = function(){
			var flag = false;
			
			var oOperation = document.getElementById("operation");
			if (oOperation) {
				oOperation.value = "doSave";
			}
			setHTMLValue();//设置html控件值
			var retvalue = doWordSave();
			if(!retvalue) {
				alert('Word文档已经被其他用户更新， 请刷新页面加载最新的Word文档！');
				return false;
			}
			var isword = false;

			if (!isword || isword == 'false') {
				flag = true;
			} else {
				if (retvalue >= 0) { // 有返回值才保存
					flag = true;
				}
			}
			if(flag){
				flag = ifSubSaveForm();
			}
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SaveCopyBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SaveCopyBtn.prototype.doAfter = function(result){
			if(!result.resultData) return;
			
			$("input[name='content.id']").val(result.resultData);
			Activity.refreshForm(this);
			
		}
		
		SaveCopyBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 关闭窗口操作
 * 
 */
function CloseWindowBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 8;
	
	
	if(typeof CloseWindowBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		CloseWindowBtn.prototype.getBeforePostData = function(){
			return jQuery("#document_content").serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		CloseWindowBtn.prototype.getActionPostData = function(){
			return jQuery("#document_content").serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		CloseWindowBtn.prototype.doBefore = function(){
			var flag = false;
			
			flag = ifSubSaveForm();
			if(flag){
				flag = beforeAct(this.actType);
			}
			return flag;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		CloseWindowBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		CloseWindowBtn.prototype.doAfter = function(result){
			if(result.resultType && (result.resultType=="message" || result.resultType=="validate" || result.resultType=="exception")){
				return;
			}
			var opentype = $("input[name='openType']").val();
			if("277"==opentype || "16"==opentype){//弹出层打开
				top.OBPM.dialog.doExit();
			}else{
				//weixin or closeTab
				if($("#openType").val()=="from_weixin_message"){//从微信待办消息中打开
					if (typeof WeixinJSBridge == "undefined"){
					    if( document.addEventListener ){
					        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
					    }else if (document.attachEvent){
					        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
					        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
					    }
					}
					try {
						WeixinJSBridge.invoke('closeWindow',{},function(res){
							//alert('关闭微信页面');
						});
					} catch (e) {
					}
					
				}
				else if(GetReferrer()==""){//判断来路为空
					try {
						WeixinJSBridge.invoke('closeWindow',{},function(res){
							//alert('关闭微信页面');
						});
					} catch (e) {
						window.close();
					}
				}
				else{
					history.go(-2);
				}
			}
		}
		
		CloseWindowBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 网页打印操作
 * 
 */
function HtmlPrintBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 14;
	
	
	if(typeof HtmlPrintBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		HtmlPrintBtn.prototype.getBeforePostData = function(){
			return jQuery("#document_content").serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		HtmlPrintBtn.prototype.getActionPostData = function(){
			return jQuery("#document_content").serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		HtmlPrintBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		HtmlPrintBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		HtmlPrintBtn.prototype.doAfter = function(result){
			var withFlowHis = false;
			var id = document.getElementsByName("content.id")[0].value;
			var applicationid = document.getElementsByName("content.applicationid")[0].value;
			var formid = document.getElementsByName("_formid")[0].value;
			var flowid = document.getElementsByName("_flowid")[0].value;
			var _templateForm = document.getElementsByName("_templateForm")[0].value;
			var signatureExist = document.getElementById("signatureExist").value;
			
			var url = contextPath + '/portal/dynaform/activity/print.action?_docid=' + id;
			url += '&_formid=' + formid;
			url += '&_signatureExist=' + signatureExist;
			url += '&application=' + applicationid;
			if (withFlowHis && flowid) {
				url += '&_flowid=' + flowid;
			}
			if(_templateForm){
				url += '&_templateForm=' + _templateForm;
			}
			if (parent != top) {
				parent.open(url);
			} else {
				window.open(url);
			}
		}
		
		HtmlPrintBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 网页打印(带流程历史)操作
 * 
 */
function HtmlPrintWithHisBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 14;
	
	
	if(typeof HtmlPrintWithHisBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		HtmlPrintWithHisBtn.prototype.getBeforePostData = function(){
			return jQuery("#document_content").serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		HtmlPrintWithHisBtn.prototype.getActionPostData = function(){
			return jQuery("#document_content").serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		HtmlPrintWithHisBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		HtmlPrintWithHisBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		HtmlPrintWithHisBtn.prototype.doAfter = function(result){
			var withFlowHis = true;
			var id = document.getElementsByName("content.id")[0].value;
			var applicationid = document.getElementsByName("content.applicationid")[0].value;
			var formid = document.getElementsByName("_formid")[0].value;
			var flowid = document.getElementsByName("_flowid")[0].value;
			var _templateForm = document.getElementsByName("_templateForm")[0].value;
			var signatureExist = document.getElementById("signatureExist").value;
			
			var url = contextPath + '/portal/dynaform/activity/print.action?_docid=' + id;
			url += '&_formid=' + formid;
			url += '&_signatureExist=' + signatureExist;
			url += '&_activityid=' + this.actId;
			url += '&application=' + applicationid;
			if (withFlowHis && flowid) {
				url += '&_flowid=' + flowid;
			}
			if(_templateForm){
				url += '&_templateForm=' + _templateForm;
			}
			if (parent != top) {
				parent.open(url);
			} else {
				window.open(url);
			}
		}
		
		HtmlPrintWithHisBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 自定义打印操作
 * 
 */
function FlexPrintBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 30;
	this.printerid = params.printerid;
	this.withFlowHis = params.withFlowHis;
	
	
	if(typeof FlexPrintBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		FlexPrintBtn.prototype.getBeforePostData = function(){
			return jQuery("#document_content").serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		FlexPrintBtn.prototype.getActionPostData = function(){
			return jQuery("#document_content").serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		FlexPrintBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		FlexPrintBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		FlexPrintBtn.prototype.doAfter = function(result){
			var id = document.getElementsByName("content.id")[0].value;
			var formid = document.getElementsByName("_formid")[0].value;
			var flowid = document.getElementsByName("_flowid")[0].value;
			var url = contextPath + '/portal/share/dynaform/printer/flexprint.jsp?_activityid='+this.actId+'&id='
			+ this.printerid + '&_docid=' + id+'&_formid='+formid+'&_flowid='+flowid;
			if (parent != top) {
				parent.open(url);
			} else {
				window.open(url);
			}
		}
		
		FlexPrintBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 导出pdf操作
 * 
 */
function ExportToPdfBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 25;
	
	
	if(typeof ExportToPdfBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		ExportToPdfBtn.prototype.getBeforePostData = function(){
			return jQuery("#document_content").serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		ExportToPdfBtn.prototype.getActionPostData = function(){
			return jQuery("#document_content").serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		ExportToPdfBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		ExportToPdfBtn.prototype.doAction = function(){
			var id = document.getElementsByName("content.id")[0].value;
			var url = contextPath + '/portal/dynaform/activity/process.action' + '?_activityid=' + this.actId;
			document.forms[0].action = url;
			document.forms[0].submit();
			
			
			return false;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		ExportToPdfBtn.prototype.doAfter = function(result){
		}
		
		ExportToPdfBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 文件下载操作
 * 
 */
function FileDownloadBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 26;
	
	
	if(typeof FileDownloadBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		FileDownloadBtn.prototype.getBeforePostData = function(){
			return jQuery("#document_content").serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		FileDownloadBtn.prototype.getActionPostData = function(){
			return jQuery("#document_content").serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		FileDownloadBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		FileDownloadBtn.prototype.doAction = function(){
			var url = contextPath + '/portal/dynaform/activity/process.action' + '?_activityid=' + this.actId;
			document.forms[0].action = url;
			document.forms[0].submit();
			
			
			return false;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		FileDownloadBtn.prototype.doAfter = function(result){
		}
		
		FileDownloadBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 电子签章操作
 * 
 */
function SignatureBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 28;
	
	
	if(typeof SignatureBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		SignatureBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		SignatureBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		SignatureBtn.prototype.doBefore = function(){
			if(navigator.userAgent.indexOf("MSIE")<=0) {
				alert("电子印章功能仅支持IE内核的浏览器!");
				return;
			}
			var ajax = null;
			if (window.ActiveXObject) {
				try {
					ajax = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {
					alert("创建Microsoft.XMLHTTP对象失败,AJAX不能正常运行.请检查您的浏览器设置.");
				}
			} else {
				if (window.XMLHttpRequest) {

					try {
						ajax = new XMLHttpRequest();
					} catch (e) {
						alert("创建XMLHttpRequest对象失败,AJAX不能正常运行.请检查您的浏览器设置.");
					}
				}
			}

			var url = document.getElementById("mGetDocumentUrl").value;
			var mLoginname = document.getElementById("mLoginname").value;
			var docid = document.getElementsByName("content.id")[0].value;
			var formid = document.getElementsByName("formid")[0].value;
			var applicationid = document.getElementsByName("applicationid")[0].value;
			url = url + "?_docid=" + docid + "&_formid=" + formid + "&_applicationid="
					+ applicationid;
			ajax.onreadystatechange = function() {
				if (ajax.readyState == 4 && ajax.status == 200) {
					if (ajax.responseText == "false") {
						return;
					}
					var documentName = ajax.responseText.split(',');
					var fildsList = "";
					for (var i = 0; i < documentName.length; i++) {
						if (i != documentName.length - 1) {
							fildsList = fildsList
									+ (documentName[i] + "=" + documentName[i] + ";");
						} else {
							fildsList = fildsList
									+ (documentName[i] + "=" + documentName[i]);
						}

					}
					if (document_content.SignatureControl != null) {
						document_content.SignatureControl.FieldsList = fildsList; // 所保护字段
						document_content.SignatureControl.Position(460, 260); // 签章位置，屏幕坐标
						document_content.SignatureControl.UserName = "lyj"; // 文件版签章用户
						document_content.SignatureControl.DivId="contentTable"; //设置签章显示位置
						document_content.SignatureControl.RunSignature(); // 执行签章操作
					} else {
						alert("请安装金格iSignature电子签章HTML版软件");
						document.getElementById("hreftest").click();
					}

				}

			};

			ajax.open("POST", url);

			ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			ajax.send(null);
			return false;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		SignatureBtn.prototype.doAction = function(){
			return false;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		SignatureBtn.prototype.doAfter = function(result){
		}
		
		SignatureBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 分享操作
 * 
 */
function TranspondBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 37;
	this.summaryCfg = params.summaryCfg;//分享摘要模板
	
	
	if(typeof TranspondBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		TranspondBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		TranspondBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		TranspondBtn.prototype.doBefore = function(){
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		TranspondBtn.prototype.doAction = function(){
			var applicationid = document.getElementsByName("content.applicationid")[0].value;
			var docid = document.getElementsByName("content.id")[0].value;
			var formid = document.getElementsByName("_formid")[0].value;
			var signatureExist = document.getElementById("signatureExist").value;
			var handleUrl = window.location.toString();
			var url = "../share/dynaform/view/activity/emailTranspond.jsp?application=" + applicationid;
			OBPM.dialog.show({
	    		opener:window.parent.parent,
	    		width: 700,
	    		height: 300,
	    		args: {"_activityid":this.actId,"application":applicationid, "docid":docid, "formid":formid, "transpond":this.summaryCfg, "handleUrl":handleUrl, "signatureExist":signatureExist},
	    		url: url,
	    		title: '转发',
	    		close: function() {
	    			
	    		}
	    	});
			return false;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		TranspondBtn.prototype.doAfter = function(result){
		}
		
		TranspondBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 跳转操作
 * 
 */
function JumpToBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 43;
	this.jumpType = params.jumpType;
	this.targetList = params.targetList;
	this.jumpMode = params.jumpMode;
	
	
	if(typeof JumpToBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		JumpToBtn.prototype.getBeforePostData = function(){
			if(jQuery("#formList").size()>0){
				return jQuery("#formList").serialize();
			}else{
				return jQuery("#document_content").serialize();
			}
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		JumpToBtn.prototype.getActionPostData = function(){
			if(jQuery("#formList").size()>0){
				return jQuery("#formList").serialize();
			}else{
				return jQuery("#document_content").serialize();
			}
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		JumpToBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		JumpToBtn.prototype.doAction = function(){
			switch (this.jumpMode) {
			case 0 :
				var olist = this.targetList.split(";");
				var formId;
				for (var i = 0; i < olist.length; i++) {
					formId = olist[0].split("|")[0];
				}
				if(this.jumpType==0){
					var applicationid = document.getElementById("application").value;
					var docid = document.getElementsByName("content.id")[0].value;
					var view_id = document.getElementById("view_id").value;
					var signatureExist = document.getElementsByName("signatureExist")[0].value;
					var formid = document.getElementsByName("formid")[0].value;
					var backUrl = document.getElementsByName("_backURL")[0].value;
					var docviewAction = contextPath + '/portal/dynaform/document/view.action';
					var newAction = contextPath + '/portal/dynaform/activity/process.action?_activityid=' + this.actId;
					var url = newAction + "&applicationid=" + applicationid + "&application=" + applicationid + "&_jumpForm=" + formid + "&_formid=" + formId + "&view_id=" + view_id + "&_isJump=1&_backURL="
							+ encodeURIComponent(docviewAction + "?_docid="+docid+"&application="+applicationid+"&_formid="+formid+"&view_id="+
									view_id+"&signatureExist="+signatureExist+"&_backURL="+encodeURIComponent(backUrl));
					window.location.href = url;
				}else{
					
				}
				
				break;
			case 1 :
				var datas = jQuery("#document_content").serialize();
				var url = contextPath + '/portal/dynaform/activity/process.action?_activityid=' + this.actId;
				var html = '<input type="hidden" name="formData" value="'+datas+'" />';
				jQuery("#document_content").append(html);
				document.forms[0].action = url;
				makeAllFieldAble();
				document.forms[0].submit();
				break;
			default :
				break;
		}
			
			return false;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		JumpToBtn.prototype.doAfter = function(result){
			
		}
		
		JumpToBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 新建操作
 * 
 */
function CreateBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 2;
	this.viewType = params.viewType;
	this.openType = params.openType;
	this.formId = params.formId;
	this.target = params.target;
	
	if(typeof CreateBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		CreateBtn.prototype.getBeforePostData = function(){
			if(typeof HAS_SUBFORM !="undefined" && HAS_SUBFORM){
				//var application = $("input[name='application']").val();
				//return $(".control-content.active").find("input,textarea,select").serialize()+"&application="+application;
				return DisplayView.span2Params(this.target);
			}else{
				return jQuery(document.forms[0]).serialize();
			}
			
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		CreateBtn.prototype.getActionPostData = function(){
			if(typeof HAS_SUBFORM !="undefined" && HAS_SUBFORM){
				var application = $(".control-content.active").find("span[name='application']").text();
				//return $(".control-content.active").find("input,textarea,select").serialize()+"&application="+application+"&formId="+this.formId;
				var view = DisplayView.getTheView(this.target);
				var json = DisplayView.span2Json(this.target);
				json['formId'] = this.formId;
				var elems = $(view).find("[name=_selects]:checked");
				json['_backURL'] = '';
				return DisplayView.addVal2Params(json,elems);
			}else{
				return jQuery(document.forms[0]).serialize()+"&formId="+this.formId;
			}
			
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		CreateBtn.prototype.doBefore = function(){
			//createDoc(this.actId);
			//if(this.openType==288 && toggleButton("button_act")) return false;
			/**
			if(this.viewType!=1){
				doNew(this.actId);
			}
			**/
			//doNew(this.actId);
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		CreateBtn.prototype.doAction = function(){
			if(this.openType==288){//网格显示
				jQuery("#VIEW_OPEN_TYPE_GRID_BTN").css("display","");
	        	DocumentUtil.doNew(this.actId, getGridParams(), function(rtn) {
	        				var oTR = eval(rtn);
	        				if (oTR) {
	        					insertRow(oTR[0], oTable.rows.length);
	        					newRows.push(oTR.attr("id"));
	        					doRowEdit(oTR.attr("id"));
	        				}

    						jQuery("#" + oTR.attr("id")).bind('change',function(){
    							setFieldValChanged(true);
    						}).bind("keydown",function(e){
    							 var key = e.which;
    							 if (this.type != "textarea" && key == 13) {
    							 	e.preventDefault(); //按enter键时阻止表单默认行为
    							 }
    						});
	        				
	        				//jquery重构按钮和控件
	        				jqRefactor();
	        				ev_resize4IncludeViewPar(); // 调整高度
	        			});
	        	return false;
			}
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		CreateBtn.prototype.doAfter = function(result){
			// View的openType(打开类型)
			var url = contextPath + "/portal/dynaform/activity/process.action?_activityid="+this.actId+"&_formid="+this.formId;
			if(this.params.parameter){
				url += "&content.id="+this.params.parameter;
			}
			if (this.openType == 17) {
				var isRelate = document.getElementsByName("isRelate");
				url += "&isSubDoc=true";
				if (isRelate){
		            if (isRelate.length>0){
		            	var relate = isRelate[0].value;
		            	url += "&isRelate="+relate;
		            }
				}
			}
		
			if (document.getElementsByName("_openType")[0]) {
				if(typeof HAS_SUBFORM !="undefined" && HAS_SUBFORM){
					this.openType = document.getElementsByName("_openType")[0].innerText;
				}else{
					this.openType = document.getElementsByName("_openType")[0].value;
				}
			}
		
			var parameters = getQueryString(this.openType);
		
			resetBackURL(); // view.js
		
			if (this.openType == 1) {
				if (isHomePage()) { // 首页单独处理
					url += "&_backURL="
							+ encodeURIComponent(contextPath
									+ "/portal/dispatch/homepage.jsp");
					url += "&" + parameters;
					parent.location = appendApplicationidByView(url);
				} else {
					document.forms[0].action = url;
					document.forms[0].submit();
				}
			} else if (this.openType == OPEN_TYPE_POP || this.openType == OPEN_TYPE_DIV) {
				var oSelects = document.getElementsByName("_selects");
				var _selects="";
				if(oSelects){
					for(var i=0;i<oSelects.length;i++){
						if(oSelects[i].checked){
							_selects+="&_selects="+oSelects[i].value;
						}
					}
				}
				url += "&" + parameters + "&openType=" + this.openType+_selects;
				url = appendApplicationidByView(url);
				
				$("#document_content").hide();
				$("#myModalexample").find(".content").css({"position":"static","height":$(window).height()-44})
				$("#myModalexample").find(".content>iframe").css({"position":"absolute","z-index":"1","top":"44px","height":$(window).height()-44})
				
				// var w = document.body.clientWidth - 25;
				// showfrontframe({
				// 			title : "",
				// 			url : url,
				// 			w : w,
				// 			h : 30,
				// 			windowObj : window.parent,
				// 			callback : function(result) {
				// 				setTimeout(function(){//通过右上角的关闭图标关闭弹出层后会显示加载进度条，加延迟后没有这个问题
				// 					document.forms[0].submit();
				// 				},1);
				// 			}
				// 		});
				MyPopup.open({
					url:url,
					title:"新建",
					success:function(){
						$("[name='_fieldid']").each(function(){
							$("#document_content").show();
							$("#"+$(this).text()).trigger("refresh");
						})
					}
				});
				

			} else if (this.openType == OPEN_TYPE_OWN) {
				var id = document.getElementsByName("_viewid")[0].value;
				if (this.openType == VIEW_TYPE_SUB) {
					var sub_divid = parent.document.getElementById('sub_divid');
					var doc_obj = parent.document.getElementById(id);
					if (sub_divid) {
						sub_divid.value = doc_obj.src;
					}
		//			if (doc_obj) {
						//url += "&" + parameters;
						//doc_obj.src = url;
						document.forms[0].action = url;
						document.forms[0].submit();
		//			}
				} else {
					document.forms[0].action = url;
					document.forms[0].submit();
				}
		
			} else {
				if(toggleButton("button_act")) return false;
			}
		}
		
		CreateBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 删除操作
 * 
 */
function DeleteBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 3;
	this.target = params.target;
	
	if(typeof DeleteBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		DeleteBtn.prototype.getBeforePostData = function(){
			if(typeof DisplayView == 'object' && DisplayView.getTheView(this.target)){
				var view = DisplayView.getTheView(this.target);
				var json = DisplayView.span2Json(this.target);
				var elems = $(view).find("[name=_selects]:checked");
				return DisplayView.addVal2Params(json,elems);
			}else{
				return jQuery(document.forms[0]).serialize();
			};
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		DeleteBtn.prototype.getActionPostData = function(){
			if(typeof DisplayView == 'object' && DisplayView.getTheView(this.target)){
				var view = DisplayView.getTheView(this.target);
				var json = DisplayView.span2Json(this.target);
				var elems = $(view).find("[name=_selects]:checked");
				return DisplayView.addVal2Params(json,elems);
			}else{
				return jQuery(document.forms[0]).serialize();
			}
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		DeleteBtn.prototype.doBefore = function(){
			var checkboxs = document.getElementsByName("_selects");
			var isSelect = false;
			for (var i = 0; i < checkboxs.length; i++) {
				if (checkboxs[i].checked == true) {
					isSelect = true;
					break;
				}
			}
			if (isSelect) {
				var rtn = window.confirm("确定要删除您选择的记录吗？");
				if (!rtn)
					return false;
			} else {
				alert("请选择要删除的数据！");
				return false;
			}
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		DeleteBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		DeleteBtn.prototype.doAfter = function(result){
			
			//获取视图类型
			var viewType = $("[name='_openType']:eq(0)").text();
			if(viewType && viewType == "277"){
				//刷新子视图
				//var $displayView = $("div[name='display_view']");
				//$displayView.bind("refresh", function(){
				//	$displayView.load($displayView.attr("src"), function(){
				//		dy_refresh(name);					
				//	});
				//}).trigger("refresh");
				
				var $thisTab = $(this.target).parents("div[name='display_view']");
				var $url = $thisTab.attr("src");

				$thisTab.trigger("refresh");
				
			}else{
				document.forms[0].action = contextPath+"/portal/dynaform/view/displayView.action";
				document.forms[0].submit();
			}
			
			
		}
		
		DeleteBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 清空操作
 * 
 */
function ClearAllBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 18;
	
	
	if(typeof ClearAllBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		ClearAllBtn.prototype.getBeforePostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		ClearAllBtn.prototype.getActionPostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		ClearAllBtn.prototype.doBefore = function(){
			return window.confirm("确定要清空表单的所有记录吗？");
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		ClearAllBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		ClearAllBtn.prototype.doAfter = function(result){
			document.forms[0].action = contextPath+"/portal/dynaform/view/displayView.action";
			document.forms[0].submit();
		}
		
		ClearAllBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 载入视图操作
 * 
 */
function QueryBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 1;
	this.viewId = params.viewId;
	this.target = params.target;
	
	
	if(typeof QueryBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		QueryBtn.prototype.getBeforePostData = function(){
			if(typeof HAS_SUBFORM !="undefined" && HAS_SUBFORM){
				return DisplayView.span2Params(this.target);
			}else{
				return jQuery(document.forms[0]).serialize();
			}
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		QueryBtn.prototype.getActionPostData = function(){
			if(typeof HAS_SUBFORM !="undefined" && HAS_SUBFORM){
				var application = $(".control-content.active").find("span[name='application']").text();
				//return $(".control-content.active").find("input,textarea,select").serialize()+"&application="+application+"&formId="+this.formId;
				var view = DisplayView.getTheView(this.target);
				var json = DisplayView.span2Json(this.target);
				json['formId'] = this.formId;
				var elems = $(view).find("[name=_selects]:checked");
				json['_backURL'] = '';
				return DisplayView.addVal2Params(json,elems);
			}else{
				return jQuery(document.forms[0]).serialize()+"&formId="+this.formId;
			}
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		QueryBtn.prototype.doBefore = function(){
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		QueryBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		QueryBtn.prototype.doAfter = function(result){
			var viewType = $("[name='_openType']:eq(0)").text();
			if(viewType && viewType == "277"){
				//刷新子视图
				//var $displayView = $("div[name='display_view']");
				//$displayView.bind("refresh", function(){
				//	$displayView.load($displayView.attr("src"), function(){
				//		dy_refresh(name);					
				//	});
				//}).trigger("refresh");
				
				var $thisTab = $(this.target).parents("div[name='display_view']");
				var $url = $thisTab.attr("src");

				$thisTab.load($url, function(){
					var $tabParameter = $(".tab_parameter");
					var viewid = this.id + "_params";
					var $viewDiv = $("<div></div>").attr("id",viewid).attr("_action",$(this).attr("src")).css("display","none");
					//表单元素转成span标签存入dom中
					$.each($(this).find(".tab_parameter").find("input[type!=button],select,textarea").serializeArray(),function(){
						$viewDiv.append("<span name='" + this.name + "'>" + this.value + "</span>"); 
					});
					$(this).append($viewDiv);
					//去掉表单元素
					$tabParameter.find("input[type!=button],select,textarea").each(function(){
						if(!$(this).attr("moduleType")) $(this).remove();
					});
					jqRefactor();					
				});
				
			}else{
				document.forms[0].action = contextPath+"/portal/dynaform/view/displayView.action?_viewid=" + this.viewId;
				document.forms[0].submit();
			}
			
		}
		
		QueryBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 批量提交操作
 * 
 */
function BatchApproveBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 20;
	this.hasRun = false;
	
	if(typeof BatchApproveBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		BatchApproveBtn.prototype.getBeforePostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		BatchApproveBtn.prototype.getActionPostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		BatchApproveBtn.prototype.doBefore = function(){
			var checkboxs = document.getElementsByName("_selects");
			var isSelect = false;
			for (var i = 0; i < checkboxs.length; i++) {
				if (checkboxs[i].checked == true) {
					isSelect = true;
					break;
				}
			}
			if(!isSelect){
				alert("至少选择一条记录！");
			}
			return isSelect;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		BatchApproveBtn.prototype.doAction = function(){
			if(this.hasRun) return true;
			var activityType = this;
			if(jQuery("#inputAuditRemarkDiv" + this.actId).attr("id")){
				artDialog.prompt('请输入审批意见：',function(val,win){
					jQuery('#_attitude' + activityType.actId).val(val);
					activityType.hasRun = true;
					Activity.doAction(activityType);
				},true);
				
			}
			
			return false;
			
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		BatchApproveBtn.prototype.doAfter = function(result){
			document.forms[0].action = contextPath+"/portal/dynaform/view/displayView.action";
			document.forms[0].submit();
		}
		
		BatchApproveBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 导出Excel操作
 * 
 */
function ExportToExcelBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 16;
	this.fileName = params.fileName;
	this.expSub = params.expSub;
	this.hasRun = false;
	
	
	if(typeof ExportToExcelBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		ExportToExcelBtn.prototype.getBeforePostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		ExportToExcelBtn.prototype.getActionPostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		ExportToExcelBtn.prototype.doBefore = function(){
			if(this.hasRun){
				alert("已导出，需要再次导出请刷新页面！");
				return false;
			}
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		ExportToExcelBtn.prototype.doAction = function(){
			var oldActionUrl = document.forms[0].action;
			var url = contextPath + '/portal/dynaform/activity/process.action' + '?_activityid=' + this.actId+ "&filename=" + this.fileName + "&isExpSub=" + this.expSub;;
			document.forms[0].action = url;
			document.forms[0].submit();
			this.hasRun = true;
			document.forms[0].action = oldActionUrl;
			return false;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		ExportToExcelBtn.prototype.doAfter = function(result){
		}
		
		ExportToExcelBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 打印视图操作
 * 
 */
function PrintViewBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 14;
	
	
	if(typeof PrintViewBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		PrintViewBtn.prototype.getBeforePostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		PrintViewBtn.prototype.getActionPostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		PrintViewBtn.prototype.doBefore = function(){
			
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		PrintViewBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		PrintViewBtn.prototype.doAfter = function(result){
			var viewid = document.getElementsByName("_viewid")[0].value;
			var signatureExist = document.getElementById("signatureExist").value;
			var url = contextPath+'/portal/dynaform/view/printView.action';
			url += '?_signatureExist=' + signatureExist;
			url += '&_activityid=' + this.actId;  
			url += '&isprint=true';
			var fmmy = document.forms[0]; 
			fmmy.action=url;
			fmwin = window.open("about:blank", "_my_submit_win");   
			fmmy.target="_my_submit_win"; 
			fmmy.submit();
			fmmy.target="";
		}
		
		PrintViewBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 导入Excel操作
 * 
 */
function ExcelImportBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 27;
	this.impmappingconfigid = params.impmappingconfigid;
	
	
	if(typeof ExcelImportBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		ExcelImportBtn.prototype.getBeforePostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		ExcelImportBtn.prototype.getActionPostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		ExcelImportBtn.prototype.doBefore = function(){
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		ExcelImportBtn.prototype.doAction = function(){
			var impcfgid = this.impmappingconfigid
			var parentid = "";
			if (document.getElementsByName("parentid")) {
				parentid = document.getElementsByName("parentid")[0].value;
			}
			
			var application = "";
			if (document.getElementsByName("application")) {
				application = document.getElementsByName("application")[0].value;
			}

			var isRelate = "";
			if(document.getElementsByName("isRelate")){
				var relateObj = document.getElementsByName("isRelate");
				if(relateObj.length != 0)
					isRelate = relateObj[0].value;
			}
			var _viewid = document.getElementsByName("_viewid")[0].value;
			if (impcfgid) {
				var url = importURL; // importURL在各页面中定义
				url += "?parentid=" + parentid;
				url += "&id=" + impcfgid;
				url += "&application=" + application;
				url += "&isRelate=" + isRelate;
				url += "&_activityid=" + this.actId;
				url += "&_viewid=" + _viewid;
				OBPM.dialog.show({
					width : 530,
					height : 420,
					url : url,
					args : {},
					title : 'Excel导入',
					close : function(result) {
						document.forms[0].submit();
					}
				});
			}
			return false;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		ExcelImportBtn.prototype.doAfter = function(result){
		}
		
		ExcelImportBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 批量签章操作
 * 
 */
function BatchSignatureBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 29;
	
	
	if(typeof BatchSignatureBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		BatchSignatureBtn.prototype.getBeforePostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		BatchSignatureBtn.prototype.getActionPostData = function(){
			return jQuery(document.forms[0]).serialize();
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		BatchSignatureBtn.prototype.doBefore = function(){
			if(navigator.userAgent.indexOf("MSIE")<0){
				alert("金格iSignature电子签章HTML版只支持IE，如果要签章请用IE浏览器");
				return;
			}
			var mLength = document.getElementsByName("_selects").length;
			var vItem;
			var DocumentList = "";
			for (var i = 0; i < mLength; i++) {
				vItem = document.getElementsByName("_selects")[i];
				if (vItem.checked) {
					if (i != mLength - 1) {
						DocumentList = DocumentList + vItem.value + ";";
					} else {
						DocumentList = DocumentList + vItem.value;
					}
				}
			}
			var ajax = null;
			if (window.ActiveXObject) {
				try {
					ajax = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {
					alert("创建Microsoft.XMLHTTP对象失败,AJAX不能正常运行.请检查您的浏览器设置.");
				}
			} else {
				if (window.XMLHttpRequest) {
					try {
						ajax = new XMLHttpRequest();
					} catch (e) {
						alert("创建XMLHttpRequest对象失败,AJAX不能正常运行.请检查您的浏览器设置.");
					}
				}
			}

			var url = document.getElementById("mGetBatchDocumentUrl").value;
			var mLoginname = document.getElementById("mLoginname").value;
			var DocumentID = document.getElementById("DocumentID").value;
			var ApplicationID = document.getElementById("ApplicationID").value
			var FormID = document.getElementById("FormID").value
			url = url + "?DocumentID=" + DocumentID + "&ApplicationID2="
					+ ApplicationID + "&FormID2=" + FormID;

			ajax.onreadystatechange = function() {
				if (ajax.readyState == 4 && ajax.status == 200) {
					if (ajax.responseText == "false") {
						return;
					}
					var documentName = ajax.responseText.split(',');
					var fildsList = "";
					for (var i = 0; i < documentName.length; i++) {
						if (i != documentName.length - 1) {
							fildsList = fildsList
									+ (documentName[i] + "=" + documentName[i] + ";");
						} else {
							fildsList = fildsList
									+ (documentName[i] + "=" + documentName[i]);
						}
					}
					if (formList.SignatureControl != null) {
						if (DocumentList == "") {
							alert("请选择需要签章的文档。");
						}
						formList.SignatureControl.FieldsList = fildsList; // 所保护字段
						formList.SignatureControl.Position(460, 275); // 签章位置
						formList.SignatureControl.DocumentList = DocumentList; // 签章页面ID
						formList.SignatureControl.WebSetFontOther("True", "同意通过", "0",
								"宋体", "11", "000128", "True"); // 默认签章附加信息及字体,具体参数信息参阅技术白皮书
						formList.SignatureControl.SaveHistory = "false"; // 是否自动保存历史记录,true保存
						formList.SignatureControl.UserName = "lyj"; // 文件版签章用户
						formList.SignatureControl.WebCancelOrder = 0; // 签章撤消原则设置,
						// 0无顺序 1先进后出
						// 2先进先出 默认值0
						// formList.SignatureControl.DivId = "contentTable"; //签章所在层
						formList.SignatureControl.AutoCloseBatchWindow = true;
						formList.SignatureControl.RunBatchSignature();
					} else {
						alert("请安装金格iSignature电子签章HTML版软件");
						document.getElementById("hreftest2").click();
					}
				}
			};
			ajax.open("POST", url);
			ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			ajax.send(null);
			return false;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		BatchSignatureBtn.prototype.doAction = function(){
			return false;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		BatchSignatureBtn.prototype.doAfter = function(result){
		}
		
		BatchSignatureBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 保存操作
 * 
 */
function ArchiveBtn(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 45;
	
	
	if(typeof ArchiveBtn._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		ArchiveBtn.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		ArchiveBtn.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		ArchiveBtn.prototype.doBefore = function(){
			return true;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		ArchiveBtn.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		ArchiveBtn.prototype.doAfter = function(result){
		}
		
		ArchiveBtn._initialized = true;
	
	}
	
	
	
	return this;
}; 
/**
 * 启动流程操作
 * 
 * @author Happy
 */
function StartWorkflow(actId,params) {
	this.actId = actId;
	this.params = params;
	this.actType = 33;
	this.editMode = params.editMode;
	this.title = params.title;
	this.hasRun = false;
	this.flowParams = "";
	
	
	if(typeof StartWorkflow._initialized == "undefined"){
		
		/**
		 * 获取执行前操作提交到后台的参数
		 */
		StartWorkflow.prototype.getBeforePostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery("#document_content").serialize();
			Activity.setFieldDisabled(fields);
			return data;
		}
		
		/**
		 * 获取执行操作提交到后台的参数
		 */
		StartWorkflow.prototype.getActionPostData = function(){
			var fields = Activity.makeAllFieldAble();
			jQuery("input[moduleType='surveyField']").obpmSurveyField("getValue");
			var data = jQuery(document.forms[0]).serialize()+"&_editMode="+this.editMode+"&"+this.flowParams;
			Activity.setFieldDisabled(fields);
			return data;
		}
		/**
		 * 按钮动作执行前的准备与校验操作
		 *（返回true时继续执行操作，返回false时停止当前操作）
		 */
		StartWorkflow.prototype.doBefore = function(){
			if(this.hasRun) return true;
			//jQuery(document.forms[0]).append("<input type='hidden' name='_editMode' value='"+this.editMode+"' />");
			if(this.editMode ==null || this.editMode==0){
				var docid = document.getElementById("_docid").value;
				var formid = document.getElementById("_formid").value;
				// var moduleid=document.getElementById("moduleid").value;
				var url = contextPath
						+ '/portal/share/workflow/runtime/startWorkFlow.jsp?_docid=' + docid
						+ '&formid=' + formid;
				var showtitle = this.title;
				if (showtitle == null || showtitle == "") {
					showtitle = "Undefind";
				}
				tempAllDisabledField();
				makeFieldState(false);
				var activityType = this;
				OBPM.dialog.show({
					width : 600, // 默认宽度
					height : 420, // 默认高度
					url : url,
					args : {},
					title : showtitle,
					close : function(result) {
						if (result) {
							activityType.flowParams = result;
							activityType.hasRun = true;
							Activity.doBefore(activityType);
						}
					}
				});
				return false;
			}else if(this.editMode==1){
				setHTMLValue(); //设置html控件值
				return true;
			}
			return false;
		}
		/**
		 * 按钮动作时执行的业务操作
		 */
		StartWorkflow.prototype.doAction = function(){
			return true;
		}
		
		/**
		 * 按钮动作执行后的业务操作
		 */
		StartWorkflow.prototype.doAfter = function(result){
		}
		
		StartWorkflow._initialized = true;
	
	}
	
	
	
	return this;
}; 
; 
(function($) {
	var isNoDeleteFile = true;
	var isReloadFile = false;
	map = {};

	function FMFileInfo(fileFullName) {
		var index = fileFullName.lastIndexOf("/");
		var webIndex = fileFullName.lastIndexOf("/");

		this.webPath = fileFullName;
		this.realName = fileFullName.substring(webIndex + 1,
				fileFullName.length);
		this.showName = fileFullName.substring(index + 1, fileFullName.length);
		this.url = contextPath + this.webPath;
	}
	
	function refreshFMFileListSub(fileFullName, uploadListId, readonly, refresh, subGridView){
		if (jQuery.trim(fileFullName) !="" ) {
			var tempFileFullName = map[uploadListId] ? map[uploadListId] : "";
			if (jQuery.trim(tempFileFullName) !="" && isNoDeleteFile && isReloadFile) {
				fileFullName = tempFileFullName + ";" + fileFullName;
			}
			jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
					.val(fileFullName);
			var divContent = '';
			divContent += '<div class="hidepic" id="' + uploadListId + 'showFileDiv"' + 'style="background:#fff;text-align:left;z-index:100000;"></div>';
			divContent += '</div>';
			var str = fileFullName.split(";");
			var tohtml = "";
			for ( var i = 0; i < str.length; i++) {

				var info2 = new FMFileInfo(str[i]);
				tohtml += '<a title="'+ info2.showName +'" href=' + info2.url + ' target="_blank" style="margin-top:2px;white-space:nowrap;text-decoration: none" >'
						+ info2.showName + '</a>'
						+ '&nbsp;<a href=# type="deleteOne" index=' + i
						+ '>';
				if (!readonly) {
					tohtml += '<img  border="0" src="../../../resource/image/close.gif"/>';
				}
				tohtml += '</a><br/>';
			}

			var $fileContent = jQuery("#" + uploadListId).html(divContent);

			$fileContent.find('.hidepic').html(tohtml);

			$fileContent.find("a[type='deleteOne']").each(function(index) {
				$(this).click(function() {
					deleteOneFMFile(index, uploadListId, readonly, refresh, subGridView);
				});
			});
			
			var time;
			
			$fileContent.mouseover(function() {
				if (time) {
					window.clearTimeout(time);
				}
			});

			divContent = "";
		} else {
			jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
			.val(fileFullName);
			jQuery("#" + uploadListId).html('');
		}
		isNoDeleteFile = true;
		isReloadFile = false;
	}

	function refreshFMFileList(fileFullName, uploadListId, readonly, refresh, subGridView) {
		refreshFMFileListSub(fileFullName, uploadListId, readonly, refresh, subGridView);
		if (refresh) {
			if(subGridView){
				dy_view_refresh(uploadListId);
			}else{
				window.setTimeout("dy_refresh('" + uploadListId.substring(uploadListId.indexOf("_") + 1) + "')",500);
			}
		}
	}
	

	function refreshFMImageListSub(fileFullName, uploadListId, myheigh, mywidth, readonly, refresh, subGridView) {
		map[uploadListId + "index"] = 0;
		map[uploadListId + "Imagewidth"] = mywidth;
		map[uploadListId + "Imageheigh"] = myheigh;

		if (jQuery.trim(fileFullName) !="") {
			var tempFileFullName = map[uploadListId] ? map[uploadListId] : "";
			if (jQuery.trim(tempFileFullName) !="" && isNoDeleteFile && isReloadFile) {
				fileFullName = tempFileFullName + ";" + fileFullName;
				map[uploadListId] = fileFullName;
			}
			map[uploadListId] = fileFullName;
			jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
					.val(fileFullName);
			var divContent = '';
			divContent += '<div class="container1" id="' + uploadListId
					+ 'div" style="width:' + mywidth + ';height:' + myheigh
					+ '">';
			divContent += '<ul class="slider1 slider2 ' + uploadListId + 'idSlider2" id="' + uploadListId + 'idSlider2">';

			var str = fileFullName.split(";");

			map[uploadListId + "maxindex"] = str.length;// map中保存相应图片控件的总数

			for ( var i = 0; i < str.length; i++) {
				var info3 = new FMFileInfo(str[i]);
				divContent += '<li><a href="' + info3.url
						+ '" rel="lightbox" title="' + info3.showName
						+ '" target="_blank">';
				if (str[i].indexOf("pdf") == -1) {
					divContent += '<img src="' + info3.url + '" width='
							+ mywidth + ' height=' + myheigh + ' border="0"/>';
				} else {
					var temp = str[i].lastIndexOf("/");
					var fileName = str[i].substr(temp + 1);
					divContent += '<font size=2 color=red>' + fileName + '</font>';
				}
				divContent += '</a></li>';
			}

			divContent += '</ul>';
			divContent += '<ul class="num">';
			divContent += '<li  title="" id="' + uploadListId
					+ 'showNumber" class="' + uploadListId + 'showNumber" style="width:30">' + 1 + '/' + str.length
					+ '</li>';
			divContent += '<li><img title="" type="previous" src="../../../resource/image/previous.png" '
			+ '/></li>';
			divContent += '<li><img title="" type="next" src="../../../resource/image/next.png"/></li><li>';
			if (!readonly) {
				divContent += '<a href=# type="delete"'
						+ '>' + '<img  border="0" src="../../../resource/image/close.gif"/>' + '</a>';
			}
			divContent += '</li></ul></div>';
			var $imageContent = jQuery("#" + uploadListId).html(divContent);
			$imageContent.find("a[type='delete']").click(function() {
				deleteOneImageFile(uploadListId, readonly, refresh, subGridView);
			});

			$imageContent.find("img[type='previous']").click(function() {
				doPrevious(uploadListId);
			});
			$imageContent.find("img[type='next']").click(function() {
				doNext(uploadListId);
			});

		} else {
			jQuery("#" + uploadListId).html('');
			map[uploadListId] = "";
			jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1)).val(fileFullName);
		}
		isNoDeleteFile = true;
		isReloadFile = false;
	}

	// 文件管理的图片文件
	function refreshFMImageList(fileFullName, uploadListId, myheigh, mywidth, readonly, refresh, subGridView) {
		refreshFMImageListSub(fileFullName, uploadListId, myheigh, mywidth, readonly, refresh, subGridView);
		if (refresh) {
			if(subGridView){
				dy_view_refresh(uploadListId);
			}else{
				window.setTimeout("dy_refresh('" + uploadListId.substring(uploadListId.indexOf("_") + 1) + "')",500);
			}
		}
	}

	// 删除一个文件
	function deleteOneFMFile(index, id, readonly, refresh,subGridView) {
			if (!readonly) {
				if(confirm("你确定删除当前文件吗？此操作不可恢复！")){
					isNoDeleteFile = false;
					isReloadFile = false;
					var str = jQuery("#" + id.substring(id.indexOf("_") + 1)).val()
							.split(";");
					var filefullname = "";
					for ( var i = 0; i < str.length; i++) {
						if (i != index) {
							filefullname += str[i] + ";";
						}
					}
					filefullname = filefullname.substring(0, filefullname
							.lastIndexOf(";"));
					var oldstr = jQuery("#" + id.substring(id.indexOf("_") + 1)).val();
					jQuery("#" + id.substring(id.indexOf("_") + 1)).val(filefullname);
					jQuery.ajax({	
						type: 'POST',
						async:false,
						url: encodeURI(encodeURI(contextPath + "/portal/upload/fileManagerdelete.action")),
						dataType : 'text',
						timeout: 3000,
						data: jQuery("#document_content").serialize(),
						success:function(x) {
							refreshFMFileList(filefullname, id, readonly, refresh,subGridView);
						},
						error: function(x) {
							jQuery("#"+ id.substring(id.indexOf("_") + 1)).val(oldstr);
						}
					});
					filefullname = "";
				}
		}
	}

	// 删除图片文件
	function deleteOneImageFile(uploadListId, readonly, refresh,subGridView) {
			if (!readonly) {
				if(confirm("你确定删除当前文件吗？此操作不可恢复！")){
					isNoDeleteFile = false;
					isReloadFile = false;
					var filefullName = "";
					var filefullNameArry = jQuery(
							"#" + uploadListId.substring(uploadListId.indexOf("_") + 1))
							.val().split(";");
		
					for ( var i = 0; i < filefullNameArry.length; i++) {
						if (i != map[uploadListId + "index"]) {
							filefullName += filefullNameArry[i] + ";";
						}
					}
					filefullName = filefullName.substring(0, filefullName
							.lastIndexOf(";"));
					var oldstr = jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1)).val();
					jQuery("#" + uploadListId.substring(uploadListId.indexOf("_") + 1)).val(filefullName);
					var myheigh = map[uploadListId + "Imageheigh"];
					var mywidth = map[uploadListId + "Imagewidth"];
					jQuery.ajax({	
						type: 'POST',
						async:false,
						url: encodeURI(encodeURI(contextPath + "/portal/upload/fileManagerdelete.action")),
						dataType : 'text',
						timeout: 3000,
						data: jQuery("#document_content").serialize(),
						success:function(x) {
							refreshFMImageList(filefullName, uploadListId, myheigh, mywidth, readonly, refresh,subGridView);
						},
						error: function(x) {
							jQuery("#"+ id.substring(id.indexOf("_") + 1)).val(oldstr);
						}
					});
				}
		}
	}

	// 图片显示效果图
	// 上一张图片
	function doPrevious(uploadListId) {
		if (map[uploadListId + "index"] > 0)
			map[uploadListId + "index"]--;
		showImg2(map[uploadListId + "index"], uploadListId);
		jQuery("." + uploadListId + "showNumber").html(
				(map[uploadListId + "index"] + 1) + "/"
						+ map[uploadListId + "maxindex"]);
	}
	// 下一张图片
	function doNext(uploadListId) {
		if (map[uploadListId + "index"] < map[uploadListId + "maxindex"] - 1)
			map[uploadListId + "index"]++;
		showImg2(map[uploadListId + "index"], uploadListId);
		jQuery("." + uploadListId + "showNumber").html(
				(map[uploadListId + "index"] + 1) + "/"
						+ map[uploadListId + "maxindex"]);
	}

	// 图片移动
	function showImg2(i, uploadListId) {
		jQuery("." + uploadListId + "idSlider2").stop(true, false).animate( {
			left : -(map[uploadListId + "Imagewidth"] * i)
		}, 800);
	}

	/**
	 * 删除文件
	 * 
	 * @param {}
	 *            valueField 文件路径保存字段
	 * @param {}
	 *            uploadListId
	 */
	function FMdeleteFile(valueField, uploadListId) {
		if(confirm("你确定删除全部文件吗？此操作不可恢复！")){
			var uploadListHtml = jQuery("#"+uploadListId).html();
			var uploadListIdValue = jQuery("#"+ uploadListId.substring(uploadListId.indexOf("_") + 1)).val();
			jQuery("#" + uploadListId).html('');
			 jQuery("#"+ uploadListId.substring(uploadListId.indexOf("_") + 1)).val("");
			jQuery.ajax({	
				type: 'POST',
				async:false,
				url: encodeURI(encodeURI(contextPath + "/portal/upload/fileManagerdelete.action")),
				dataType : 'text',
				timeout: 3000,
				data: jQuery("#document_content").serialize(),
				success:function(x) {
				},
				error: function(x) {
					//恢复数据
					jQuery("#" + uploadListId).html(uploadListHtml);
					 jQuery("#"+ uploadListId.substring(uploadListId.indexOf("_") + 1)).val(uploadListIdValue);
				}
			});	
			valueField.value = ''; // 清空值
			map[uploadListId] = "";
			isReUpload = true;
		}
	}

	/**
	 * 文件管理
	 * 
	 * @param {}
	 *            pathname 文件目录
	 * @param {}
	 *            pathFieldId 文件路径域ID
	 * @param {}
	 *            viewid 视图ID
	 * @param {}
	 *            allowedTypes 允许上传的类型
	 * @param {}
	 *            maximumSize 最大值
	 * @param {}
	 *            layer 布局
	 * @param {}
	 *            fileSaveMode 文件保存模式
	 * @return {} 文件网络路径
	 */
	function FileManager(pathname, pathFieldId, viewid, allowedTypes,
			maximumSize, fileSaveMode, callback, applicationid) {
		var url = contextPath
				+ '/portal/share/component/filemanager/filemanager.jsp?path='
				+ pathname;
		var oField = document.getElementById(pathFieldId);

		if (allowedTypes) {
			url += '&allowedTypes=' + allowedTypes;
		}

		if (maximumSize) {
			url += '&maximumSize=' + maximumSize;
		}

		if (fileSaveMode) {
			// 自定义
			url += '&fileSaveMode=' + fileSaveMode;
		} else {
			// 系统
			url += '&fileSaveMode=' + 00;
		}
		// 系统
		url += '&applicationid=' + applicationid;

		hiddenDocumentFieldIncludeIframe();// in util.js
		showfrontframe({
			title : 'File Manager',
			url : url,
			w : 1000,
			h : 600,
			callback : function(result) {
				showDocumentFieldIncludeIframe();// //in util.js

				var rtn = result;
				if (result == null || result == 'undefined') {
					if (oField && oField.value) {
						isReloadFile = false;
						rtn = oField.value;
					} else {
						rtn = '';
					}
				} else {
					isReloadFile = true;
				}
				oField.value = rtn;
				if (callback && typeof (callback) == "function") {
					callback();
				}
			}
		});
	}

	$.fn.obpmFileManager = function() {
		return this.each(function() {
			var $field = jQuery(this);
			var id = $field.attr("id");
			var name =$field.attr("name");
			var fieldType =$field.attr("fieldType");
			var value = $field.attr("value");
			var maxsize = $field.attr("maxsize");
			var limitType = $field.attr("limitType");
			var disabled = $field.attr("disabled");
			var path = $field.attr("path");
			var fileSaveMode = $field.attr("fileSaveMode");
			var application = $field.attr("application");
			var discript = HTMLDencode($field.attr("discript"));
			var readonly = (disabled == 'disabled');
			var imgWidth = $field.attr("imgWidth");
			var imgHeight = $field.attr("imgHeight");
			var uploadList = $field.attr("uploadList");
			var refreshOnChanged = $field.attr("refreshOnChanged");
			var fileManagerLabel = $field.attr("fileManagerLabel");
			var fileRemoveLabel = $field.attr("fileRemoveLabel");
			var subGridView = $field.attr("subGridView");
			subGridView =(subGridView=='true');
			
			var html = "<table><tr>";
				if(limitType == "file"){
					html +="<td style='border:0'>";
					html +="<div id='" + uploadList + "' GridType='fileManager'></div>";
					html +="</td>";
				}
				html +="<td style='border:0'>";
				if(limitType == "image"){
					html +="<div id='" + uploadList + "' GridType='fileManager'></div>";
				}
				html+="<input type='hidden' id='" + id +"' name='" + name + "' fieldType='" + fieldType + "' value='" + value + "' />";
				html += "<input type='button' value='" + fileManagerLabel
					+ "' name='btnFileManager' class='btnFileManager'";

			if (disabled && disabled != '') {
				html += " disabled='" + disabled + "' ";
			}
			var _callback = function() {
				if (limitType == "file") {
					refreshFMFileList(document.getElementById(id).value,
							uploadList, readonly, refreshOnChanged, subGridView);
				} else {
					refreshFMImageList(document.getElementById(id).value,
							uploadList, imgHeight, imgWidth, readonly,
							refreshOnChanged, subGridView);
				}
			},
			init = function(){
				if (limitType == "file") {
					refreshFMFileListSub(document.getElementById(id).value,
							uploadList, readonly, refreshOnChanged, subGridView);
				} else {
					refreshFMImageListSub(document.getElementById(id).value,
							uploadList, imgHeight, imgWidth, readonly,
							refreshOnChanged, subGridView);
				}
			};
			
			html += "/>";
			// 删除
			html += "<input class='FMdeleteFile' type='button' value='"
					+ fileRemoveLabel + "' name='btnDelete'";

			if (disabled && disabled != '') {
				html += " disabled='" + disabled + "' ";
			}

			html += "/>";
			html += "</td>";
			html += "<td style='border:0'>"+ discript + "</td>";
			html += "</tr></table>";
			var $html=$(html);
			$html.find(".btnFileManager").click(function() {
					FileManager(path, id, '_viewid', limitType,maxsize, fileSaveMode, _callback,application);
			}).end().find(".FMdeleteFile").click(function() {
						FMdeleteFile(document.getElementById(id), uploadList);
			});
			$field.replaceWith($html);
			init();
		});
	};

	$.fn.obpmViewFileManager = function(){
		return this.each(function(){
			var $field = jQuery(this);
			var imgw = $field.attr("imgw");
			var imgh = $field.attr("imgh");
			var str = $field.attr("str");
			var uploadListId = $field.attr("uploadListId");
			var viewReadOnly = $field.attr("viewReadOnly");
			map[uploadListId + "index"] = 0;
			map[uploadListId + "maxindex"] = str;
			map[uploadListId + "Imagewidth"] = imgw;
			viewReadOnly = (viewReadOnly=="true")?true:false;
			var imgwHalf = imgh/2;
			var isSubGridView = jQuery("#obpm_subGridView").size()>0?true:false;

			var html = "";
			html += "<div class='container1' id='" + uploadListId + "div' style='width:" + imgw + "; height:117px;'>";
			html += "<ul class='slider1 slider2 " + uploadListId + "idSlider2' id='" + uploadListId + "idSlider2'>";
			$field.find("[subType='aField']").each(function(){
				var $a = jQuery(this);
				var docId = $a.attr("docId");
				var docFormid = $a.attr("docFormid");
				var url = $a.attr("url");
				var fileName = $a.attr("fileName");
				html +="<li style='position:relative;' class='imgList'>";
				if(viewReadOnly){
					html += "<img border='0' alt='" + fileName +"'";
					html += " src='" + url + "' width='" + imgw + "' height='" + imgh + "' />";
				}else{
					html +="<a";
					if(!isSubGridView){
						html +=" href='javaScript:viewDoc(\"" + docId + "\",\"" + docFormid + "\")'";
					}
					html += " title='" + fileName + "'><img border='0' alt='" + fileName +"'";
					html += " src='" + url + "' width='" + imgw + "' height='" + imgh + "' /></a>";
				}
				html += "<div class='showImgIcon' style='display:none;position:absolute;right:0px;top:"+imgwHalf+"px;z-index:100;'><a cancelBubble = true;  class='imgClick' href='" + url + "' target='blank'><img alt='" + fileName + "' border='0' src='";
				html += "../../../resource/images/picture_go.png' title='点击查看原图' /></a><div></li>";
			});
			html += "</ul>";
			html += "<ul class='num'>";
			html += "<li  title='' id='" + uploadListId + "showNumber' class='" + uploadListId + "showNumber' style='width:30;'>" + 1 + "/" + str + "</li>";
			html += " <li><img title='' type='previous' src='../../../resource/image/previous.png' /></li>";
			html += " <li><img title='' type='next' src='../../../resource/image/next.png'/></li>";
			html += "</ul></div>";
			var $html=$(html);
			$html.find(".imgList").mouseover(function(event) {
				event.stopPropagation();
				jQuery(this).find(".showImgIcon").show();
			}).mouseout(function(event){
				event.stopPropagation();
				jQuery(this).find(".showImgIcon").hide();
			});
			$html.find("img[type='previous']").click(function(event) {
				event.stopPropagation();
				doPrevious(uploadListId);
			});
			$html.find("img[type='next']").click(function(event) {
				event.stopPropagation();
				doNext(uploadListId);
			});
			$field.replaceWith($html);
		});
	};
})(jQuery);; 
; 
; 
(function($){
	$.fn.obpmHtmlEditorField=function(){
		return this.each(function() {
			var $field = jQuery(this);
			var displayType = $field.attr("displayType");
			var text = URLDecode($field.html());
			var style = $field.attr("style");
			var name = $field.attr("name");
			var discript = $field.attr("_discript");
			discript = discript? discript : name;
			var html = "";
			if (displayType == PermissionType_HIDDEN) {
				$(html).replaceAll($field);
			} else if (displayType != PermissionType_MODIFY) {
				html += "<label for='" + name + "'>" + discript + "</label>";
				html += "<div class='html-edit' readonly='readonly' name='" + name + "' >";
				if (text != "null")
					html += text;
				html += "</div>";
				$(html).replaceAll($field);
			} else {
				var $html = $("<div class='formfield-wrap'><label class='field-title contact-name' for='" + name + "'>" + discript + "</label>");
				var $iframeDiv = $("<div class='html-edit' id='" + name + "' name='" + name + "'></div>");
				var iframe = document.createElement("iframe");
				iframe.style.cssText = "border: black thin;width:100%; height:200px";
				iframe.setAttribute("class", "html-edit-panel");
				$iframeDiv.append(iframe).appendTo($html);
				$field.after($html);
				var doc = iframe.contentDocument;
				doc.designMode = "On";
				if (text != "null" && text != ""){
					setTimeout(function(){
						doc.write(text);
					},15);
				}
				$field.attr("moduletype","htmlEditorSave");
			}
		});
	};
})(jQuery);
; 
(function($){
	$.fn.obpmSelectAboutField =function(){
		return this.remove();//左右选择框在手机端不显示
		return this.each(function(){
			var $field =jQuery(this);
			var id=$field.attr("id");
			var name =$field.attr('name');
			var isRefreshOnChanged = $field.attr("isRefreshOnChanged");
			var displayType=$field.attr("displayType");
			var hiddenValue =  $field.attr("hiddenValue");
			var textType=$field.attr("textType");
			var value = $field.val();
			var discript = $field.attr("_discript");
			
			value = value ? value : "";
			discript = discript? discript : name;
			isRefreshOnChanged=(isRefreshOnChanged=='true');
			
			var html = "";
			if(displayType != PermissionType_HIDDEN){
				//刷新重计算时不重构
				if(!$field.attr("isLoaded"))
					$field.attr("isLoaded",true);
				else return;
				$field.before("<label for='" + name + "'>" + discript + "</label>");
				$field.bind("change",function(){
					if(isRefreshOnChanged){
						//dy_refresh(this.id);刷新方法在jquery.multiselect2side.js调用
					}
				});
				if(displayType == PermissionType_READONLY || textType.toLowerCase() == "readonly" || displayType == PermissionType_DISABLED){
					jQuery('#'+id).multiselect2side({selectedPosition: 'right',moveOptions: false,labelsx: '',labeldx: '',readOnly:true});
				}else{
					jQuery('#'+id).multiselect2side({selectedPosition: 'right',moveOptions: false,labelsx: '',labeldx: '',readOnly:false});
				}
			}else{
				html +="<input type='hidden' name='" + name + "' id='" + id + "' value='" + value + "' />";
				html +="<div>" + hiddenValue + "</div>";
				$field.after(html);
				$field.remove();
			}
		});
	};
	
})(jQuery);

//刷新方法，在jquery.multiselect2side.js调用
function callRefresh(el){
	var $field = jQuery(el);
	var isRefreshOnChanged = $field.attr("isRefreshOnChanged");
	isRefreshOnChanged=(isRefreshOnChanged=='true');
	if(isRefreshOnChanged){
		dy_refresh($field.id);
	}
}; 
/*
 * multiselect2side jQuery plugin
 *
 * Copyright (c) 2010 Giovanni Casassa (senamion.com - senamion.it)
 *
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 *
 * http://www.senamion.com
 *
 */

(function($)
{
	jQuery.fn.multiselect2side = function (o) {

		o = $.extend({
			selectedPosition: 'right',
			moveOptions: true,
			labelTop: 'Top',
			labelBottom: 'Bottom',
			labelUp: 'Up',
			labelDown: 'Down',
			labelSort: 'Sort',
			labelsx: 'Available',
			labeldx: 'Selected',
			maxSelected: -1,
			readOnly:false
		}, o);

		// 当没有选项时，为控件选中一个空文本的选项
		var addEmptyOptionWhenNoSelect = function(oSelect) {
			if (oSelect.find("option:selected").size() == 0){
				oSelect.find("[text='']").attr("selected", true);
			} else {
				oSelect.find("[text='']").attr("selected", false);
			}
		};
		
		return this.each(function () {
			var	el = $(this);

			var	originalName = $(this).attr("name");
			if (originalName.indexOf('[') != -1)
				originalName = originalName.substring(0, originalName.indexOf('['));

			var	nameDx = originalName + "ms2side__dx";
			var	nameSx = originalName + "ms2side__sx";
			var size = $(this).attr("size");
			// SIZE MIN
			if (size < 5) {
				$(this).attr("size", "5");
				size = 5;
			}

			// UP AND DOWN
			var divUpDown =
					"<div class='ms2side__updown'>" +
						"<p class='SelSort' title='Sort'>" + o.labelSort + "</p>" +
						"<p class='MoveTop' title='Move on top selected option'>" + o.labelTop + "</p>" +
						"<p class='MoveUp' title='Move up selected option'>" + o.labelUp + "</p>" +
						"<p class='MoveDown' title='Move down selected option'>" + o.labelDown + "</p>" +
						"<p class='MoveBottom' title='Move on bottom selected option'>" + o.labelBottom + "</p>" +
					"</div>";

			// CREATE NEW ELEMENT (AND HIDE IT) AFTER THE HIDDED ORGINAL SELECT
			var htmlToAdd = 
				"<span class='ms2side__div'>" +
						((o.selectedPosition != 'right' && o.moveOptions) ? divUpDown : "") +
						"<table><tbody><tr><td class='ms2side__select'>" +
						(o.labelsx ? ("<span class='ms2side__header'>" + o.labelsx + "</span>") : "");
			if(o.readOnly){
				htmlToAdd += "<select data-enhance='false' title='" + o.labelsx + "' name='" + nameSx + "' id='" + nameSx + "' size='" + size + "' multiple='multiple' disabled='disabled'></select>";
			}else{
				htmlToAdd += "<select data-enhance='false' title='" + o.labelsx + "' name='" + nameSx + "' id='" + nameSx + "' size='" + size + "' multiple='multiple'></select>";
			}
			
			htmlToAdd += "</td>" +
					"<td class='ms2side__options'>" +
						((o.selectedPosition == 'right')
						?
						("<table style='display: inline;vertical-align:bottom;'><tr><td><p class='AddOne' title='Add Selected'>&rsaquo;</p></td></tr>" +
						"<tr><td><p class='AddAll' title='Add All'>&raquo;</p></td></tr>" +
						"<tr><td><p class='RemoveOne' title='Remove Selected'>&lsaquo;</p></td></tr>" +
						"<tr><td><p class='RemoveAll' title='Remove All'>&laquo;</p></td></tr></table>")
						:
						("<table style='display: inline;vertical-align:bottom'><tr><td><p class='AddOne' title='Add Selected'>&lsaquo;</p></td></tr>" +
						"<tr><td><p class='AddAll' title='Add All'>&laquo;</p></td></tr>" +
						"<tr><td><p class='RemoveOne' title='Remove Selected'>&rsaquo;</p></td></tr>" +
						"<tr><td><p class='RemoveAll' title='Remove All'>&raquo;</p></td></tr></table>")
						) +
					"</td>" +
					"<td class='ms2side__select'>" +
						(o.labeldx ? ("<span class='ms2side__header'>" + o.labeldx + "</span>") : "");
			if(o.readOnly){
				htmlToAdd += "<select data-enhance='false' title='" + o.labeldx + "' name='" + nameDx + "' id='" + nameDx + "' size='" + size + "' multiple='multiple' disabled='disabled'></select>";
			}else{
				htmlToAdd += "<select data-enhance='false' title='" + o.labeldx + "' name='" + nameDx + "' id='" + nameDx + "' size='" + size + "' multiple='multiple'></select>";
			}
			htmlToAdd += "</td></tr></tbody></table>" +
					((o.selectedPosition == 'right' && o.moveOptions) ? divUpDown : "") +
				"</span>";
			$(this).after(htmlToAdd).hide();

			// ELEMENTS
			var allSel = $(this).next().find("select");
			var	leftSel = (o.selectedPosition == 'right') ? allSel.eq(0) : allSel.eq(1);
			var	rightSel = (o.selectedPosition == 'right') ? allSel.eq(1) : allSel.eq(0);
			// HEIGHT DIV
			var	heightDiv = $(".ms2side__select").eq(0).height();
			
			// CENTER MOVE OPTIONS AND UPDOWN OPTIONS
			$(this).next().find('.ms2side__options, .ms2side__updown').each(function(){
				var	top = ((heightDiv/2) - ($(this).height()/2));
				if (top > 0)
					$(this).css('padding-top',  top + 'px' );
			})

			// MOVE SELECTED OPTION TO RIGHT, NOT SELECTED TO LEFT
			// 初始化时过滤掉空文本选项
			$(this).find("option:selected[text!='']").clone().appendTo(rightSel);
			$(this).find("option:not(:selected)[text!='']").clone().appendTo(leftSel);

			// SELECT FIRST LEFT ITEM
//			if (!($.browser.msie && $.browser.version == '6.0'))
				leftSel.find("option").eq(0).attr("selected", true);

			// ON CHANGE REFRESH ALL BUTTON STATUS
			allSel.change(function() {
				var	div = $(this).parent().parent();
				var	selectSx = leftSel.children();
				var	selectDx = rightSel.children();
				var	selectedSx = leftSel.find("option:selected");
				var	selectedDx = rightSel.find("option:selected");

				if (selectedSx.size() == 0 ||
						(o.maxSelected >= 0 && (selectedSx.size() + selectDx.size()) > o.maxSelected))
					div.find(".AddOne").addClass('ms2side__hide');
				else
					div.find(".AddOne").removeClass('ms2side__hide');

				// FIRST HIDE ALL
				div.find(".RemoveOne, .MoveUp, .MoveDown, .MoveTop, .MoveBottom, .SelSort").addClass('ms2side__hide');
				if (selectDx.size() > 1)
					div.find(".SelSort").removeClass('ms2side__hide');
				if (selectedDx.size() > 0) {
					div.find(".RemoveOne").removeClass('ms2side__hide');
					// ALL SELECTED - NO MOVE
					if (selectedDx.size() < selectDx.size()) {	// FOR NOW (JOE) && selectedDx.size() == 1
						if (selectedDx.val() != selectDx.val())	// FIRST OPTION, NO UP AND TOP BUTTON
							div.find(".MoveUp, .MoveTop").removeClass('ms2side__hide');
						if (selectedDx.last().val() != selectDx.last().val())	// LAST OPTION, NO DOWN AND BOTTOM BUTTON
							div.find(".MoveDown, .MoveBottom").removeClass('ms2side__hide');
					}
				}

				if (selectSx.size() == 0 ||
						(o.maxSelected >= 0 && selectSx.size() >= o.maxSelected))
					div.find(".AddAll").addClass('ms2side__hide');
				else
					div.find(".AddAll").removeClass('ms2side__hide');

				if (selectDx.size() == 0)
					div.find(".RemoveAll").addClass('ms2side__hide');
				else
					div.find(".RemoveAll").removeClass('ms2side__hide');
				
				if(o.readOnly){
					div.find(".AddOne, .AddAll, .RemoveOne, .RemoveAll").addClass('ms2side__hide');
				}
			});

			// DOUBLE CLICK ON LEFT SELECT OPTION
			leftSel.dblclick(function () {
				$(this).find("option:selected").each(function(i, selected){

					if (o.maxSelected < 0 || rightSel.children().size() < o.maxSelected) {
						$(this).remove().appendTo(rightSel);
						el.find("[value=\"" + $(selected).val() + "\"]").attr("selected", true).remove().appendTo(el);
					}
				});
				$(this).trigger('change');
				//调用刷新方法  obpm.selectAboutField.js
				callRefresh(el);
			});

			// DOUBLE CLICK ON RIGHT SELECT OPTION
			rightSel.dblclick(function () {
				$(this).find("option:selected").each(function(i, selected){
					$(this).remove().appendTo(leftSel);
					el.find("[value=\"" + $(selected).val() + "\"]").attr("selected", false).remove().appendTo(el);
				});
				addEmptyOptionWhenNoSelect(el);
				$(this).trigger('change');
				//调用刷新方法  obpm.selectAboutField.js
				callRefresh(el);
			});

			// CLICK ON OPTION
			$(this).next().find('.ms2side__options p').click(function () {
				if (!$(this).hasClass("ms2side__hide")) {
					if ($(this).hasClass("AddOne")) {
						leftSel.find("option:selected").each(function(i, selected){
							$(this).remove().appendTo(rightSel);
							el.find("[value=\"" + $(selected).val() + "\"]").attr("selected", true).remove().appendTo(el);
						});
					}
					else if ($(this).hasClass("AddAll")) {	// ALL SELECTED
						leftSel.children().appendTo(rightSel);
						leftSel.children().remove();
						el.find('option').attr("selected", true);
						// el.children().attr("selected", true); -- PROBLEM WITH OPTGROUP
					}
					else if ($(this).hasClass("RemoveOne")) {
						rightSel.find("option:selected").each(function(i, selected){
							$(this).remove().appendTo(leftSel);
							el.find("[value=\"" + $(selected).val() + "\"]").attr("selected", false).remove().appendTo(el);
						});
						addEmptyOptionWhenNoSelect(el);
					}
					else if ($(this).hasClass("RemoveAll")) {	// ALL REMOVED
						rightSel.children().appendTo(leftSel);
						rightSel.children().remove();
						el.find('option').each(function(){
							if(jQuery(this).val())
								jQuery(this).attr("selected",false);
							else
								jQuery(this).attr("selected",true);//没有选中只是设置空值
						});
						addEmptyOptionWhenNoSelect(el);
						//el.children().attr("selected", false); -- PROBLEM WITH OPTGROUP
					}
					//调用刷新方法  obpm.selectAboutField.js
					callRefresh(el);

				}

				leftSel.trigger('change');
			});

			// CLICK ON UP - DOWN
			$(this).next().find('.ms2side__updown').children().click(function () {
				var	selectedDx = rightSel.find("option:selected");
				var	selectDx = rightSel.find("option");

				if (!$(this).hasClass("ms2side__hide")) {
					if ($(this).hasClass("SelSort")) {
						// SORT SELECTED ELEMENT
						selectDx.sort(function(a, b) {
							 var compA = $(a).text().toUpperCase();
							 var compB = $(b).text().toUpperCase();
							 return (compA < compB) ? -1 : (compA > compB) ? 1 : 0;
						})
						// FIRST REMOVE FROM ORIGINAL SELECT
						el.find("option:selected").remove();
						// AFTER ADD ON ORIGINAL AND RIGHT SELECT
						selectDx.each(function() {
							rightSel.append($(this).clone().attr("selected", true));
							el.append($(this).attr("selected", true));
						});
					}
					else if ($(this).hasClass("MoveUp")) {
						var	prev = selectedDx.first().prev();
						var	hPrev = el.find("[value=" + prev.val() + "]");

						selectedDx.each(function() {
							$(this).insertBefore(prev);
							el.find("[value=" + $(this).val() + "]").insertBefore(hPrev);	// HIDDEN SELECT
						});
					}
					else if ($(this).hasClass("MoveDown")) {
						var	next = selectedDx.last().next();
						var	hNext = el.find("[value=" + next.val() + "]");

						selectedDx.each(function() {
							$(this).insertAfter(next);
							el.find("[value=" + $(this).val() + "]").insertAfter(hNext);	// HIDDEN SELECT
						});
					}
					else if ($(this).hasClass("MoveTop")) {
						var	first = selectDx.first();
						var	hFirst = el.find("[value=" + first.val() + "]");

						selectedDx.each(function() {
							$(this).insertBefore(first);
							el.find("[value=" + $(this).val() + "]").insertBefore(hFirst);	// HIDDEN SELECT
						});
					}
					else if ($(this).hasClass("MoveBottom")) {
						var	last = selectDx.last();
						var	hLast = el.find("[value=" + last.val() + "]");

						selectedDx.each(function() {
							last = $(this).insertAfter(last);	// WITH last = SAME POSITION OF SELECTED OPTION AFTER MOVE
							hLast = el.find("[value=" + $(this).val() + "]").insertAfter(hLast);	// HIDDEN SELECT
						});
					}
				}

				leftSel.trigger('change');
			});

			// HOVER ON OPTION
			$(this).next().find('.ms2side__options, .ms2side__updown').children().hover(
				function () {
					$(this).addClass('ms2side_hover');
				},
				function () {
					$(this).removeClass('ms2side_hover');
				}
			);

			// UPDATE BUTTON ON START
			leftSel.trigger('change');
			// SHOW WHEN ALL READY
			$(this).next().show();
		});
	};
})(jQuery);; 
(function($) {
	$.fn.obpmSuggestField = function() {
		return this.each(function(i) {
			var TEXT_TYPE_HIDDEN = "hidden",
				TEXT_TYPE_READONLY = "readonly",
				TEXT_TYPE_TEXT = "text",
				TEXT_TYPE_PASSWORD = "password";
			
			var $field = jQuery(this);
			var fieldId = $field.attr("_fieldId");
			var name = $field.attr("_name");
			var isRefreshOnChanged = $field.attr("_isRefreshOnChanged");
			var displayType = $field.attr("_displayType");
			var textType = $field.attr("_textType");
			var value = $field.attr("value");
			var text = $field.attr("text");
			if(text == "" || text == null){
				text = value;
			}
			var json = $field.attr("_json")? $field.attr("_json") : '{}';
			var otherAttrs = $field.attr("_otherAttrs");
			var style=$field.attr("style");
			var fieldType = $field.attr("_fieldType");
			var fieldId2 = fieldId.replace(/\-/g, "_");
			var subGridView = $field.attr("_subGridView");
			var discript = $field.attr("_discript");
			var hiddenValue = $field.attr("_hiddenValue");
			var dataMode = $field.attr("_dataMode");
			var formId = $field.attr("_formid");
			var _fieldId4sych = $field.attr("_fieldId4sych");
			var domainId = $field.attr("_domainId");

			isRefreshOnChanged = (isRefreshOnChanged == "true");
			subGridView = (subGridView == "true");
			value = value ? value : "";
			discript = discript? discript : name;
			
			if(style){
				style = style.replace(/"([^"]*)"/g, "$1"); //把样式中包含的双引号全部替换为空，以免在放入style时发生引号冲突导致样式代码段被截断
			}
			//if(displayType != PermissionType_HIDDEN){
				var otherAttrsHtml = getOtherAttrs($field[0]);//其他属性
				
				//span
				var $spanHtml = $("<div class='formfield-wrap'></div>");
				var htmlShow = "";
	
				//text input
				if(textType.toLowerCase() != TEXT_TYPE_HIDDEN && displayType != PermissionType_HIDDEN){
					$spanHtml.append("<label class='field-title contact-name' for='" + name + "'>" + discript + "</label>");
					var inputHtml = "<input type='text' class='contactField form-control' GridType='suggestField' value='" + value + "' id='" + fieldId + "_show'"
						+ " name='" + name + "_show'" + "fieldtype = '" + fieldType + "'" + " autocomplete='off'"
						+ " " + getOtherProps(otherAttrs) + otherAttrsHtml;
						
					inputHtml += " style=\"";	//style
					if(style) inputHtml += style + ";";
					inputHtml +=" z-index:97;";
					
					if(displayType == PermissionType_READONLY || TEXT_TYPE_READONLY == textType.toLowerCase() 
							|| displayType == PermissionType_DISABLED){
						inputHtml += "display:none;";
						htmlShow += "<div id='"+ name +"_show1' class='showItem'>" + value + "</div>";
					}
					inputHtml += "\"";
					inputHtml += " />";
					var $input = $(inputHtml);
					$spanHtml.append($input);
					$input.val(text);
					if(dataMode == "local"){
						var source = eval(json) || [];
						$input.typeahead({
							source:source, 
							autoSelect: true,
							menu: '<ul class="typeahead dropdown-menu" style="width:95%" role="listbox"></ul>',
							item: '<li><a href="#" role="option"></a></li>',
							delay: 100,
							afterSelect : function(current){
				                  if (current) {
				                      if (current.id != $("#"+fieldId).val()) {
				                    	  $("#"+fieldId).val(current.id);//设值
				                    	  if(isRefreshOnChanged){
												if(subGridView)
													dy_view_refresh(fieldId + "_show");
												else
													dy_refresh(fieldId + "_show");
											}
				                      }
				                  }
							}
						}); 
					}else if(dataMode == "remote"){
						var url = contextPath + "/portal/document/suggestfield/query?_formFieldId="+_fieldId4sych;
						$input.typeahead({
							source:function(query,process) {
								var fields = Activity.makeAllFieldAble();
								var data = jQuery("#document_content").serialize()+"&__keyword="+query;
								Activity.setFieldDisabled(fields);
								$.get(url,data,function(json){
									process(eval(json));
								});
						    }, 
							autoSelect: true,
							menu: '<ul class="typeahead dropdown-menu" style="width:95%" role="listbox"></ul>',
							item: '<li><a href="#" role="option"></a></li>',
							delay: 500,
							afterSelect : function(current){
				                  if (current) {
				                      if (current.id != $("#"+fieldId).val()) {
				                    	  $("#"+fieldId).val(current.id);//设值
				                    	  if(isRefreshOnChanged){
												if(subGridView)
													dy_view_refresh(fieldId + "_show");
												else
													dy_refresh(fieldId + "_show");
											}
				                      }
				                  }
							}
						}); 
					}
				}
				var discriptHtml="";
				if(displayType == PermissionType_HIDDEN){
					discriptHtml += "<span>"+ hiddenValue +"</span>";
				}
				//hidden input
				var hiddenHtml = "<input type='hidden' value='" + value + "' id='" + fieldId + "' name='" + name + "'" + "fieldtype = '" + fieldType + "'" + " GridType='suggestField' resetable=true />";
				$spanHtml.append($(hiddenHtml)).append(htmlShow).append($(discriptHtml)).replaceAll($field);
			//}
		});
	};

})(jQuery);
; 
!function(a,b){"use strict";"undefined"!=typeof module&&module.exports?module.exports=b(require("jquery")):"function"==typeof define&&define.amd?define(["jquery"],function(a){return b(a)}):b(a.jQuery)}(this,function(a){"use strict";var b=function(b,c){this.$element=a(b),this.options=a.extend({},a.fn.typeahead.defaults,c),this.matcher=this.options.matcher||this.matcher,this.sorter=this.options.sorter||this.sorter,this.select=this.options.select||this.select,this.autoSelect="boolean"==typeof this.options.autoSelect?this.options.autoSelect:!0,this.highlighter=this.options.highlighter||this.highlighter,this.render=this.options.render||this.render,this.updater=this.options.updater||this.updater,this.displayText=this.options.displayText||this.displayText,this.source=this.options.source,this.delay=this.options.delay,this.$menu=a(this.options.menu),this.$appendTo=this.options.appendTo?a(this.options.appendTo):null,this.shown=!1,this.listen(),this.showHintOnFocus="boolean"==typeof this.options.showHintOnFocus?this.options.showHintOnFocus:!1,this.afterSelect=this.options.afterSelect,this.addItem=!1};b.prototype={constructor:b,select:function(){var a=this.$menu.find(".active").data("value");if(this.$element.data("active",a),this.autoSelect||a){var b=this.updater(a);this.$element.val(this.displayText(b)||b).change(),this.afterSelect(b)}return this.hide()},updater:function(a){return a},setSource:function(a){this.source=a},show:function(){var b,c=a.extend({},this.$element.position(),{height:this.$element[0].offsetHeight});return b="function"==typeof this.options.scrollHeight?this.options.scrollHeight.call():this.options.scrollHeight,(this.$appendTo?this.$menu.appendTo(this.$appendTo):this.$menu.insertAfter(this.$element)).css({top:c.top+c.height+b,left:c.left}).show(),this.shown=!0,this},hide:function(){return this.$menu.hide(),this.shown=!1,this},lookup:function(b){if(this.query="undefined"!=typeof b&&null!==b?b:this.$element.val()||"",this.query.length<this.options.minLength)return this.shown?this.hide():this;var c=a.proxy(function(){a.isFunction(this.source)?this.source(this.query,a.proxy(this.process,this)):this.source&&this.process(this.source)},this);clearTimeout(this.lookupWorker),this.lookupWorker=setTimeout(c,this.delay)},process:function(b){var c=this;return b=a.grep(b,function(a){return c.matcher(a)}),b=this.sorter(b),b.length||this.options.addItem?(b.length>0?this.$element.data("active",b[0]):this.$element.data("active",null),this.options.addItem&&b.push(this.options.addItem),"all"==this.options.items?this.render(b).show():this.render(b.slice(0,this.options.items)).show()):this.shown?this.hide():this},matcher:function(a){var b=this.displayText(a);return~b.toLowerCase().indexOf(this.query.toLowerCase())},sorter:function(a){for(var b,c=[],d=[],e=[];b=a.shift();){var f=this.displayText(b);f.toLowerCase().indexOf(this.query.toLowerCase())?~f.indexOf(this.query)?d.push(b):e.push(b):c.push(b)}return c.concat(d,e)},highlighter:function(b){var c,d,e,f,g,h=a("<div></div>"),i=this.query,j=b.toLowerCase().indexOf(i.toLowerCase());if(c=i.length,0===c)return h.text(b).html();for(;j>-1;)d=b.substr(0,j),e=b.substr(j,c),f=b.substr(j+c),g=a("<strong></strong>").text(e),h.append(document.createTextNode(d)).append(g),b=f,j=b.toLowerCase().indexOf(i.toLowerCase());return h.append(document.createTextNode(b)).html()},render:function(b){var c=this,d=this,e=!1;return b=a(b).map(function(b,f){var g=d.displayText(f);return b=a(c.options.item).data("value",f),b.find("a").html(c.highlighter(g)),g==d.$element.val()&&(b.addClass("active"),d.$element.data("active",f),e=!0),b[0]}),this.autoSelect&&!e&&(b.first().addClass("active"),this.$element.data("active",b.first().data("value"))),this.$menu.html(b),this},displayText:function(a){return a.name||a},next:function(b){var c=this.$menu.find(".active").removeClass("active"),d=c.next();d.length||(d=a(this.$menu.find("li")[0])),d.addClass("active")},prev:function(a){var b=this.$menu.find(".active").removeClass("active"),c=b.prev();c.length||(c=this.$menu.find("li").last()),c.addClass("active")},listen:function(){this.$element.on("focus",a.proxy(this.focus,this)).on("blur",a.proxy(this.blur,this)).on("keypress",a.proxy(this.keypress,this)).on("keyup",a.proxy(this.keyup,this)),this.eventSupported("keydown")&&this.$element.on("keydown",a.proxy(this.keydown,this)),this.$menu.on("click",a.proxy(this.click,this)).on("mouseenter","li",a.proxy(this.mouseenter,this)).on("mouseleave","li",a.proxy(this.mouseleave,this))},destroy:function(){this.$element.data("typeahead",null),this.$element.data("active",null),this.$element.off("focus").off("blur").off("keypress").off("keyup"),this.eventSupported("keydown")&&this.$element.off("keydown"),this.$menu.remove()},eventSupported:function(a){var b=a in this.$element;return b||(this.$element.setAttribute(a,"return;"),b="function"==typeof this.$element[a]),b},move:function(a){if(this.shown){switch(a.keyCode){case 9:case 13:case 27:a.preventDefault();break;case 38:if(a.shiftKey)return;a.preventDefault(),this.prev();break;case 40:if(a.shiftKey)return;a.preventDefault(),this.next()}a.stopPropagation()}},keydown:function(b){this.suppressKeyPressRepeat=~a.inArray(b.keyCode,[40,38,9,13,27]),this.shown||40!=b.keyCode?this.move(b):this.lookup()},keypress:function(a){this.suppressKeyPressRepeat||this.move(a)},keyup:function(a){switch(a.keyCode){case 40:case 38:case 16:case 17:case 18:break;case 9:case 13:if(!this.shown)return;this.select();break;case 27:if(!this.shown)return;this.hide();break;default:this.lookup()}a.stopPropagation(),a.preventDefault()},focus:function(a){this.focused||(this.focused=!0,this.options.showHintOnFocus&&this.lookup(""))},blur:function(a){this.focused=!1,!this.mousedover&&this.shown&&this.hide()},click:function(a){a.stopPropagation(),a.preventDefault(),this.select(),this.$element.focus()},mouseenter:function(b){this.mousedover=!0,this.$menu.find(".active").removeClass("active"),a(b.currentTarget).addClass("active")},mouseleave:function(a){this.mousedover=!1,!this.focused&&this.shown&&this.hide()}};var c=a.fn.typeahead;a.fn.typeahead=function(c){var d=arguments;return"string"==typeof c&&"getActive"==c?this.data("active"):this.each(function(){var e=a(this),f=e.data("typeahead"),g="object"==typeof c&&c;f||e.data("typeahead",f=new b(this,g)),"string"==typeof c&&(d.length>1?f[c].apply(f,Array.prototype.slice.call(d,1)):f[c]())})},a.fn.typeahead.defaults={source:[],items:8,menu:'<ul class="typeahead dropdown-menu" role="listbox"></ul>',item:'<li><a href="#" role="option"></a></li>',minLength:1,scrollHeight:0,autoSelect:!0,afterSelect:a.noop,addItem:!1,delay:0},a.fn.typeahead.Constructor=b,a.fn.typeahead.noConflict=function(){return a.fn.typeahead=c,this},a(document).on("focus.typeahead.data-api",'[data-provide="typeahead"]',function(b){var c=a(this);c.data("typeahead")||c.typeahead(c.data())})});; 
//DD Tab Menu- Last updated April 27th, 07: http://www.dynamicdrive.com
//Only 1 configuration variable below

var ddtabmenu={
	disabletablinks: false, ////Disable hyperlinks in 1st level tabs with sub contents (true or false)?
	currentpageurl: window.location.href.replace("http://"+window.location.hostname, "").replace(/^\//, ""), //get current page url (minus hostname, ie: http://www.dynamicdrive.com/)
	tabOrigitemid: {},

definemenu:function(tabid, dselected){
	this[tabid+"-menuitems"]=null;
	ddtabmenu.init(tabid,dselected);		
//		this.addEvent(window, function(){ddtabmenu.init(tabid, dselected)}, "load")
},

/**
 * 根据页签ID选中某个页签
 * @param {} tabid
 * @param {} targetId 相当于Form ID
 */
showsubmenuById:function(tabid, targetId) {
	var menuitems=this[tabid+"-menuitems"];
 	if (!menuitems) {
 		return;
 	}

 	if(targetId != "")//当所有选项都隐藏时，没有默认选中项
 		ddtabmenu.showsubmenu(tabid, document.getElementById(targetId));
},

/**
 * 根据页签名称选中某个页签
 * @param {} tabid
 * @param {} targetName 控件定制时的页签名称
 */
showsubmenuByName:function(tabid, targetName) {
	var menuitems=this[tabid+"-menuitems"];
 	if (!menuitems) {
 		return;
 	}
 	
 	for (i=0; i<menuitems.length; i++){
		if (menuitems[i].getAttribute("title")){
			if (menuitems[i].title == targetName){
				ddtabmenu.showsubmenu(tabid, menuitems[i]);
				break;
			}
		}
	}
},

/**
 * 选中某一个页签
 * @param {} tabid
 * @param {} targetitem 目标页签
 */
showsubmenu:function(tabid, targetitem){
	if(document.getElementById("tabid")){
		var tempTabid = document.getElementById("tabid").value;
		if(tempTabid == ""){
			document.getElementById("tabid").value = tabid + "#" + targetitem.getAttribute("id");
		}else{
			var ids = document.getElementById("tabid").value.split(";");
			var flag = true;
			for(var i=0; i<ids.length; i++){
				var _ids = ids[i].split("#");
				if(tabid == _ids[0]){
					_ids[1] = targetitem.getAttribute("id");
					ids[i] = _ids.join("#");
					flag = false;
					break;
				}
			}
			
			if(flag){
				document.getElementById("tabid").value = tempTabid + ";" + tabid + "#" + targetitem.getAttribute("id");
			}else{
				document.getElementById("tabid").value = ids.join(";");
			}
		}
	}
	var menuitems=this[tabid+"-menuitems"];
 	if (!menuitems) {
 		return;
 	}
	// 运行计算时使用同步,使用了DWR库
	DWREngine.setAsync(false);
	if(targetitem != null)
		eval(targetitem.callback);
	DWREngine.setAsync(true);
	
	for (i=0; i<menuitems.length; i++){
		
		if (typeof menuitems[i].hasSubContent!="undefined"){
			if(document.getElementById(menuitems[i].getAttribute("rel")).style.display=='block'){
				menuitems[i].className="btn btn-positive btn-outlined";
				document.getElementById(menuitems[i].getAttribute("rel")).style.display="none";
			}
		}
	}
	if (targetitem) {
		if (typeof targetitem.hasSubContent!="undefined")
			var id =targetitem.getAttribute("rel");
		var oContent = document.getElementById(targetitem.getAttribute("rel"));
		if(getBrowser().indexOf("ie")!=-1){
			var frames = jQuery("#"+id).find("iframe[type='word']");
			for(var i =0 ;i<frames.length;i++){
				var frame = frames[i];
				initWord(frame);
			}
		}
		
		if(oContent != null || oContent != undefined){
			if(oContent.style.display !='block'){
				targetitem.className="btn btn-positive current";
				oContent.style.display="block";
				
				//jack for iframe lazy load
				jQuery(oContent).find("[moduleType='IncludedView']").obpmIncludedView();
			}
		}
		var isloaded = oContent!=null?oContent.isloaded:false;
		// 记录选中页签
//		this.tabOrigitemid[tabid] = targetitem.id;
//		if (isloaded != true) {
//			// 运行计算时使用同步,使用了DWR库
//			DWREngine.setAsync(false);
//			eval(targetitem.callback);
//			DWREngine.setAsync(true);
//		}
	}
	//选项卡iframe中引用（前台管理界面）时重新加载一次iframe页面，以解决宽高问题。
	var iframeObj = document.getElementsByTagName("iframe")[0];
	if(iframeObj){
		if(typeof iframeObj.contentWindow.adjustUserSetupPageLayout=="function"){
			iframeObj.contentWindow.document.location.reload(); 
		};
	}
	var tid = $(targetitem).attr("id");
	$("#form_tab").find("[_tid]").hide();
	$("#form_tab").find("a[href='#item1mobile']").hide();
	$("#document_content").css("margin-top","0px")
	if ($("#form_tab").find("[_tid='"+tid+"']").size()>0){
		$("#form_tab").find("a[href='#item1mobile']").show();
		$("#form_tab").find("[_tid='"+tid+"']").show();
		$("#document_content").css("margin-top","36px")
	}
},

/**
 * 隐藏页签及页签内容
 * @param {} tabid
 * @param {} menuids
 */
hideMenus: function (tabid, menuids){
	for (var i = 0; i < menuids.length; i++) {
		var menu = document.getElementById("li_" + menuids[i]);
		var content = document.getElementById("content_" + menuids[i]);
		menu.style.display = "none";
		content.style.display = "none";
	}
},

/**
 * 显示页签及页签内容
 * @param {} tabid
 * @param {} menuids
 */
showMenus: function (tabid, menuids){
	for (var i = 0; i < menuids.length; i++) {
		var menu = document.getElementById("li_" + menuids[i]);
		var content = document.getElementById("content_" + menuids[i]);
		menu.style.display = "block";
		content.style.display = "block";
	}
},

/**
 * 是否已加载
 * @param {} tabid
 * @return {Boolean}
 */
isloaded: function(tabid) {
	var selectedEl = document.getElementById("content_" + this.tabOrigitemid[tabid]);
	if (selectedEl) {
		return selectedEl.isloaded;
	}
	
	return false;
},

/**
 * 获取选中页签
 * @param {} tabid
 * @return {}
 */
getSelected: function(tabid){
	return this.tabOrigitemid[tabid];
},

isSelected:function(menuurl){
	var menuurl=menuurl.replace("http://"+menuurl.hostname, "").replace(/^\//, "")
	return (ddtabmenu.currentpageurl==menuurl);
},

addEvent:function(target, functionref, tasktype){ //assign a function to execute to an event handler (ie: onunload)
	var tasktype=(window.addEventListener)? tasktype : "on"+tasktype
	if (target.addEventListener)
		target.addEventListener(tasktype, functionref, false);
	else if (target.attachEvent)
		target.attachEvent(tasktype, functionref);
},

/**
 * 初始化页签控件
 * @param {} tabid 页签控件ID
 * @param {} dselected 选中页签的名称
 */
init:function(tabid, dselected){
	if (!document.getElementById(tabid)) {
		alert("ddtabmenu.js: tab not exist, id is \"" + tabid + "\"");
	}
	var menuitems=document.getElementById(tabid).getElementsByTagName("a");
	var setalready=false;
	this[tabid+"-menuitems"]=menuitems;
	for (var x=menuitems.length-1; x>=0; x--){
		if (menuitems[x].getAttribute("rel")){
			//alert("if x="+x);
			this[tabid+"-menuitems"][x].hasSubContent=true;
			if (ddtabmenu.disabletablinks){
				alert("if1 if x="+x);
				menuitems[x].onclick=function(){return false;}
			}
		}
		else{//for items without a submenu, add onMouseout effect
			alert("else x="+x);
			menuitems[x].onmouseout=function(){this.className="";}
		}
		menuitems[x].onclick=function(){
			ddtabmenu.showsubmenu(tabid, this);
			try{
				var conSelId = "content_" + this.id;
				jQuery("#content_" + this.id + " iframe[name=display_view]").each(function(){
					if(typeof(eval(jQuery(this)[0].contentWindow.ev_resize4IncludeViewPar))=="function")
						jQuery(this).get(0).contentWindow.ev_resize4IncludeViewPar();//设置包含视图元素的高度
				});
			}catch(ex){
				alert("tabmenu: 重设包含元素iframe高度脚本错误");
			}
		};
		if (dselected=="auto" && typeof setalready=="undefined" && this.isSelected(menuitems[x].href)){
			alert("if2 x="+x);
			ddtabmenu.showsubmenu(tabid, menuitems[x]);
			setalready = true;
		}
	}
	
	// 默认显示第一个页签
	if (!setalready) {
		ddtabmenu.showsubmenuById(tabid, dselected);
	}
}
}; 
(function($){
	$.fn.obpmTabNormalField =function(){
		return this.each(function(){
			var $field = jQuery(this);
			$field.removeAttr("moduleType");
			
			//选项卡嵌套时递归重构
			$field.find("[moduleType='TabNormal']").filter(function(){
				return (jQuery(this).parents("[moduleType='TabNormal']").size() == 0);
				//}).obpmTabNormalField();  		//页签选项卡
			}).obpmTabCollapseField(); 
			//选项卡嵌套时递归重构
			$field.find("[moduleType='TabCollapse']").filter(function(){
				return (jQuery(this).parents("[moduleType='TabCollapse']").size() == 0);
			}).obpmTabCollapseField();  		//折叠选项卡
			
			var html = "";
			// title
			var $fieldTitle = $field.find("[id='title']");

			var tabId=$field.attr("tabId");
			var fieldId=$fieldTitle.attr("fieldId");
			var titleDiv = "<div id='" + fieldId + "' class='basictab' style='margin-bottom: 10px;border-bottom: 1px solid #CACACA;padding-bottom: 10px;overflow-x: auto;'>";
			var titleUl = "<table cellSpacing='0' cellPadding='0'><tr>";
			$fieldTitle.find("div").each(function(){
				var formId=$(this).attr("formId");
				var isHidden=$(this).attr("isHidden");
				var isRefreshOnChanged=$(this).attr("isRefreshOnChanged");
				var name=$(this).attr("name");
				isRefreshOnChanged = (isRefreshOnChanged == "true");
				isHidden = (isHidden == "true");
								
				// 判断选项卡内是否含有非包含元素的控件
				var $moduleType = $field.find(".tabcontainer>div[formId='"+formId+"']").find("[moduleType]");
				var $calctext =  $field.find(".tabcontainer>div[formId='"+formId+"']").find(".calctext-field");
				if($moduleType.not("[moduleType=IncludedView]").size()==0 && $calctext.size()==0){
					isHidden = true;
				}

				// <li>
				var liHtml = "";

				liHtml += "<td><div id='li_" + formId + "'";

				if(isHidden){
					liHtml += " style='display:none'";
				}
				liHtml += ">";
				
				// <a>
				var aHtml = "";
				aHtml += "<a class='btn btn-positive btn-outlined' style='margin-right:10px;cursor:pointer'";
				if(isRefreshOnChanged){
					//aHtml += " callback=dy_refresh('" + formId + "')";
				}
				aHtml += " id='" + formId + "'";
				aHtml += " rel='content_" + formId + "'";
				aHtml += " title='" + name + "'>";
				aHtml += name + "</a>";
				liHtml += aHtml;
				liHtml += "</div></td>";
				titleUl += liHtml;
			});
			titleUl += "</tr></table>";
			titleDiv += titleUl;
			titleDiv += "</div>";
			
			// content
			$field.find("#tabcontainer > div").each(function(i){
				var formId=$(this).attr("formId");
				var isHidden=$(this).attr("isHidden");
				isHidden = (isHidden == "true");
				jQuery(this).attr("id","content_" + formId + "").addClass("tabcontent");
				if(i>=0){
					jQuery(this).hide();
				}
				if(isHidden){
					jQuery(this).css("display","none");	
					var tabIncludedID = $(this).find("[moduletype='IncludedView']").attr("_fieldid");
					$("body").find("a.control-item[href='#tab_"+tabIncludedID+"']").hide();
				}				
			});
			jQuery(titleDiv).replaceAll($fieldTitle);
			
			//初始化
			ddtabmenu.definemenu(fieldId,tabId);
		});
	};
	
	$.fn.obpmTabCollapseField =function(){
		return this.each(function(){
			var $field = jQuery(this);
			$field.addClass("subNavBox");
			$field.removeAttr("moduleType");
			//选项卡嵌套时递归重构
			$field.find("[moduleType='TabNormal']").filter(function(){
				return (jQuery(this).parents("[moduleType='TabNormal']").size() == 0);
			//}).obpmTabNormalField();  		//页签选项卡
			}).obpmTabCollapseField(); 
			//选项卡嵌套时递归重构
			$field.find("[moduleType='TabCollapse']").filter(function(){
				return (jQuery(this).parents("[moduleType='TabCollapse']").size() == 0);
			}).obpmTabCollapseField();  		//折叠选项卡
			
			var fieldId = $field.attr("fieldId");
			var tabIdList = $field.find("#tabIdList").text();
			//字符串转数组
			tabIdList = eval(tabIdList);
			$field.find("#tabIdList").remove();
			var html = "";
			$field.find("li").each(function(){
				jQuery(this).css("listStyle","none");
				//title
				$(this).find("#title").each(function(){
					var formId = $(this).attr("formId");
					var isHidden = $(this).attr("isHidden");
					var fieldId = $(this).attr("fieldId");
					var tabName = $(this).attr("tabName");
					var isOpen = $(this).attr("isOpen");
					isHidden = (isHidden == "true");
					isOpen = (isOpen == "true");
					//div
					var titleDivHtml = "";
					titleDivHtml += "<div id='li_" + formId + "'";
					if(isHidden){
						titleDivHtml += " style='display:none'";
						this.parentNode.style.display = "none";	//ie下面当有选项卡隐藏后，中间还是留有间隔，所以隐藏。
					}
					titleDivHtml += ">";
					//table
					var tableHtml = "";
					tableHtml = "<div class='subNav' id='zlight-nav'>"
					tableHtml += "<span class='icon icon-down up-color'></span>"
					tableHtml += tabName
					tableHtml += "</div>"




					// var imgId = "img_" + formId;
					// tableHtml += "<table width='100%' height='22' align='center' cellPadding='0' cellSpacing='0' id='" + formId
					// 	+ "' class='margin' onclick=toggleCollapse('" + fieldId + "',this.id)>";
					// tableHtml += "<tr>";

					// var imagePath = "../../share/component/tabField/collapse/images";
					// tableHtml += "<td width='5' align='left'><img src='" + imagePath + "/left_l_bar.gif'/></td>";
					// tableHtml += "<td width='30' align='center' background='" + imagePath + "/left_bar.gif'>";
					// tableHtml += "<img id='" + imgId + "' src='" + imagePath + "/";
					// if(isOpen)
					// 	tableHtml += "left_open";
					// else
					// 	tableHtml += "left_dot";
					// tableHtml += ".gif' /></td>";
					// tableHtml += "<td class='white' background='" + imagePath + "/left_bar.gif'>" + tabName + "</td>";
					// tableHtml += "<td width='5' align='right'><img src='" + imagePath + "/left_r_bar.gif'/></td>";
					// tableHtml += "</tr>";
					// tableHtml += "</table>";
					
					titleDivHtml += tableHtml;
					titleDivHtml += "</div>";
					html += titleDivHtml;
					jQuery(titleDivHtml).replaceAll(jQuery(this));
				});
				//content
				$(this).find("#content").each(function(){
					var formId=$(this).attr("formId");
					var isHidden=$(this).attr("isHidden");
					isHidden = (isHidden == "true");
					jQuery(this).attr("id","content_" + formId + "");
					jQuery(this).addClass("navContent");
					if(isHidden){
						jQuery(this).css("display","none");	
					}


				});

				$(window).scroll(function(){
					var subNavTop = $(this).scrollTop();
					$(".subNavBox>li").each(function() {

						if($(this).offset().top <= subNavTop){

							$(this).children("div[id^='li_']").addClass("navTitle").css({'position':'fixed','top':'0px'});
							
							$(this).children("div[id^='content_']").css({'padding-top':$(this).children("div[id^='li_']").outerHeight()+10+'px'});
							if($(this).next().offset().top - subNavTop <= $(this).children("div[id^='li_']").outerHeight()){
								$(this).children("div[id^='li_']").css({'position':'absolute','top':$(this).next().offset().top-$(this).children("div[id^='li_']").outerHeight()});
			
							}
						}else{
							$(this).children("div[id^='li_']").removeClass("navTitle").css({'position':'static'});
							$(this).children("div[id^='content_']").css({'padding-top':'10px'});
						}

						//console.log("this-top:"+$(this).next().offset().top+",scrolltop:"+subNavTop+",childheight:"+$(this).children("div[id^='li_']").outerHeight());
        	
        			});
				});	
			});
			
			$(this).find(".navContent").each(function(){
				if($(this).text()==""){
					$(this).parent("li").hide();
				}
			})
			//初始化
			defineCollapse(fieldId,tabIdList);

			$(".subNav").click(function(){
				$(this).parent().toggleClass("icon-border-hide");
				$(this).find('span').toggleClass("icon-right").siblings(".icon").removeClass("icon-right");
				$(this).find('span').toggleClass("icon-down up-color").siblings(".icon").removeClass("icon-down up-color");
				$(this).parent().next(".navContent").slideToggle(500).siblings(".navContent").slideUp(500);
			});
		});			
	};
})(jQuery);

; 
var TAB_CONTENT = "content_";
var TAB_IAMGE = "img_";
var selectedTabMap = {};

/**
 * 定义折叠容器
 * 
 * @param {}
 *            container
 * @param {}
 *            tabArray
 * @param {}
 *            selectedIndex
 */
function defineCollapse(container, tabArray) {
	if (document.readyState == 'complete') {
		initCollapse(container, tabArray);
	} else {
		OBPM(document).ready(function(){
			initCollapse(container, tabArray);
		});
		
//		addEvent(window, function() {init(container, tabArray);}, "load");
	}
}
/**
 * 添加事件
 * 
 * @param {}
 *            target
 * @param {}
 *            functionref
 * @param {}
 *            tasktype
 */
function addEvent(target, functionref, tasktype) {
	var tasktype = (window.addEventListener) ? tasktype : "on" + tasktype
	if (target.addEventListener)
		target.addEventListener(tasktype, functionref, false)
	else if (target.attachEvent)
		target.attachEvent(tasktype, functionref)
}

/**
 * 初始化显示
 * 
 * @param {}
 *            container
 * @param {}
 *            tabArray
 * @param {}
 *            selectedIndex
 */
function initCollapse(container, tabArray) {
	selectedTabMap[container] = [];
	for (var i = 0; i < tabArray.length; i++) {
		var content_id = TAB_CONTENT + tabArray[i];// 表格ID
		var oContent = document.getElementById(content_id);

		selectedTabMap[container].push(tabArray[i]); // 已选中
		oContent.isloaded = true; // 已加载
	}
}

/**
 * 开关
 * 
 * @param {}
 *            container
 * @param {}
 *            tabId
 */
function toggleCollapse(container, tabId, noRefresh) {// 显示隐藏程序
	var content_id = TAB_CONTENT + tabId;// 表格ID
	var img_id = TAB_IAMGE + tabId;// 图片ID
	var imagePath = "../../share/component/tabField/collapse/images";
	var default_img = "" + imagePath + "/left_dot.gif";// 默认图片
	var on_img = "" + imagePath + "/left_open.gif";// 打开时图片
	var off_img = "" + imagePath + "/left_dot.gif";// 隐藏时图片
	var oContent = document.getElementById(content_id);
	var oImgage = document.getElementById(img_id);

	if (oContent.style.display == "none") {
		selectedTabMap[container].push(tabId); // 选中
	} else {
		selectedTabMap[container].pop(tabId); // 弹出
	}

	// 刷新
	var isloaded = oContent.isloaded;
	var refreshed = false;
	// alert("1");
	/*
	if (isloaded != true) {
		if (!noRefresh) {
			// 运行计算时使用同步,使用了DWR库
			DWREngine.setAsync(false);
			dy_refresh(tabId);
			DWREngine.setAsync(true);
			if(oContent.style.display != "none"){
				refreshed = true;
			}
		}

		oContent.isloaded = true;
	}
	*/

	if (!refreshed) {
		if (oContent.style.display == "none") {// 如果为隐藏状态
			oContent.style.display = "";// 切换为显示状态
			oImgage.src = on_img;
		}// 换图
		else {// 否则
			oContent.style.display = "none";// 切换为隐藏状态
			oImgage.src = off_img;
		}// 换图
	}

}

function getCollapseSelectedArray(container) {
	return selectedTabMap[container];
}

function getCollapseSelected(container) {
	return getCollapseSelectedArray(container).toString();
}

function isCollapseLoaded(container) {
	var selecteds = getCollapseSelectedArray(container);
	var rtn = {};
	for (var i = 0; i < selecteds.length; i++) {
		var selectedEl = document.getElementById(TAB_CONTENT + selecteds[i]);
		if (selectedEl) {
			if (selectedEl.isloaded) {
				rtn[selecteds[i]] = true;
			} else {
				rtn[selecteds[i]] = false;
			}
		}
	}
	return jQuery.json2Str(rtn);
}

/**
 * 显示所有Tab
 * 
 * @param {}
 *            container
 */
function showAllCollapse(container) {
	OBPM('#tab_' + container + '_divid').children("div[id*='content_']:hidden")
			.each(function() {
						var tabId = this.id.substring(TAB_CONTENT.length);
						toggleCollapse(container, tabId);
					})
}

/**
 * 关闭所有Tab
 * 
 * @param {}
 *            container
 */
function closeAllCollapse(container) {
	OBPM('#tab_' + container + '_divid')
			.children("div[id*='content_']:visible").each(function() {
						var tabId = this.id.substring(TAB_CONTENT.length);
						toggleCollapse(container, tabId, true);
					});
}; 
/**
 * 选择当前节点审批人
 * 
 * @param {}
 *            actionName
 * @param {}
 *            fieldId
 */
function selectField(actionName, fieldId) {
	var oCurridArray = document.getElementsByName("_currid");
	// 当前节点ID
	var currid = '';
	if (oCurridArray && oCurridArray.length > 0) {
		currid = oCurridArray[0].value;
	}
	// 目标文本框
	var oFiled = document.getElementById(fieldId);
	if (oFiled) {
		var map = oFiled.value ? jQuery.parseJSON(oFiled.value) : {};
		
		var defValue = oFiled.value;
		var rtn = showUserSelectNoFlow('', {
					defValue: map[currid],
					callback: function(result) {
						// prototype1_6.js
						if (result) {
							var userlist = result.split(",");
							// 为当前节点设置
							map[currid] = userlist;
							oFiled.value = jQuery.json2Str(map);
						}
					}
				});
	}
}

/**
 * 选择用户
 * 
 * @param {}
 *            actionName
 * @param {}
 *            settings
 */
function showUserSelectNoFlow(actionName, settings, roleid) {
	var oApp = document.getElementsByName("application")[0];
	var url = contextPath + "/portal/phone/resource/component/dialog/selectUserByAll.jsp?application="+oApp.value;
	var setValueOnSelect = true;
	if (settings.setValueOnSelect == false) {
		setValueOnSelect = settings.setValueOnSelect;
	}
	
	OBPM.dialog.show({
				width : 610,
				height : 450,
				url : url,
				args : {
					// p1:当前窗口对象
					"parentObj" : window,
					// p2:存放userid的容器id
					"targetid" : settings.valueField,
					// p3:存放username的容器id
					"receivername" : settings.textField,
					// p4:默认选中值, 格式为[userid1,userid2]
					"defValue": settings.defValue,
					//选择用户数
					"limitSum": settings.limitSum,
					//选择模式
					"selectMode":settings.selectMode,
					// 存放userEmail的容器id
					"receiverEmail" : settings.showUserEmail,
					// 存放userEmail的容器id
					"receiverEmailAccount" : settings.showUserEmailAccount,
					// 存放userTelephone的容器id
					"receiverTelephone" : settings.showUserTelephone
				},
				title : title_uf,
				close : function(result) {
					var textObj = document.getElementById(settings.textField);
//					if(textObj != null){
//						textObj.style.border = "1px solid #ff0000";
//					}
					if(result){
						var rtnValue = "";
						for(var i = 0; i < result.length; i++){
							rtnValue += result[i].value + ';';
						}
						var rtnText = "";
						for(var i = 0; i < result.length; i++){
							rtnText += result[i].text + ';';
						}
						rtnValue = rtnValue.substring(0,rtnValue.length-1);
						rtnText = rtnText.substring(0,rtnText.length-1);
						$("#"+settings.valueField).val(rtnValue);
						$("#"+settings.textField).val(rtnText);
					}
					if (settings.callback) {
						if(typeof settings.callback == "function"){
							settings.callback(rtnValue);
						}else{//用户选择框控件
							eval(settings.callback);
						}
					}
				}
			});
}

/**
 * 选择部门
 * 
 * @param {}
 *            actionName
 * @param {}
 *            settings
 */
function showDepartmentSelect(actionName, settings) {
	var url = contextPath + '/portal/phone/resource/component/dialog/select.jsp';
	var valueField = document.getElementById(settings.valueField);
	var value = "";
	if (valueField) {
		value = valueField.value;
	}
	// 使用jquery-adapter
	OBPM.dialog.show({
				width : 300,
				height : 400,
				url : url,
				args : {
					value : value,
					readonly : settings.readonly,
					limit : settings.limit
				},
				title : title_df,
				close : function(result) {
					var textObj = document.getElementById(settings.textField);
//					if(textObj != null){
//						textObj.style.border = "1px solid #ff0000";
//					}
					var rtn = result;
					var field = document.getElementById(settings.textField);
					if (field) {
						if (rtn) {
							if(rtn =="clear"){// 清空
								field.value = '';
								valueField.value = '';
							}else{
								var rtnValue = '';
								var rtnText = '';

								if (rtn[0] && rtn.length > 0) {
									for (var i = 0; i < rtn.length; i++) {
										rtnValue += rtn[i].value + ";";
										rtnText += rtn[i].text + ";";
									}

									rtnValue = rtnValue.substring(0, rtnValue
													.lastIndexOf(";"));
									rtnText = rtnText.substring(0, rtnText
													.lastIndexOf(";"));
								}
								field.value = rtnText;
								valueField.value = rtnValue;
							}
						}

						if (settings.callback) {
							settings.callback(valueField.name);
						}
					}
					
				}
			});
}
; 
; 
var OBPM = top.jQuery;
(function($) {
	// 弹出框 
	OBPM.dialog = OBPM.dialog ? OBPM.dialog : {
		_allOptions: [],
		opts : {
			args:{},					//弹出层中通过args参数名传过来的参数(兼容原弹出层插件)
			content: '',				// 消息内容
			title: '',					// 标题. 默认'消息'
			button: null,				// 自定义按钮
			ok: null,					// 确定按钮回调函数
			cancel: null,				// 取消按钮回调函数
			init: null,					// 对话框初始化后执行的函数
			close: null,				// 对话框关闭前执行的函数
			okVal: '\u786E\u5B9A',		// 确定按钮文本. 默认'确定'
			cancelVal: '\u53D6\u6D88',	// 取消按钮文本. 默认'取消'
			width: '',					// 内容宽度
			height: '',					// 内容高度
			minWidth: 96,				// 最小宽度限制
			minHeight: 32,				// 最小高度限制
			padding: '20px 25px',		// 内容与边界填充距离
			skin: '',					// 皮肤名(预留接口,尚未实现)
			icon: '',					// 消息图标名称
			time: null,					// 自动关闭时间
			esc: true,					// 是否支持Esc键关闭
			focus: true,				// 是否支持对话框按钮自动聚焦
			show: true,					// 初始化后是否显示对话框
			follow: null,				// 跟随某元素(即让对话框在元素附近弹出)
			path: '',					// artDialog路径
			lock: true,					// 是否锁屏
			background: '#000',			// 遮罩颜色
			opacity: 0.5,				// 遮罩透明度
			duration: 300,				// 遮罩透明度渐变动画速度
			fixed: true,				// 是否静止定位
			left: '0%',				// X轴坐标
			top: '0%',				// Y轴坐标
			zIndex: 1987,				// 对话框叠加高度值(重要：此值不能超过浏览器最大限制)
			resize: true,				// 是否允许用户调节尺寸
			drag: true					// 是否允许用户拖动位置
		},
		/**
		 * 显示弹出框
		 */
		show : function(options) {
			if(!options.args)options.args = {};
			
			//初始化时最大化
			if(options.maximization == true){
				options.height = $(top.window).height()-50;
				options.width = $(top.window).width();
			}
			
			
			//宽度和高度值处理
			if(options.width && options.width>0){
				options.width = options.width+"";	//转成字符
				options.width = (options.width.indexOf("%")>0 && options.width.indexOf("px")>0)?options.width:(options.width+"px");
			}
			if(options.height && options.height>0){
				options.height = options.height+"";	//转成字符
				options.height = (options.width.indexOf("%")>0 && options.width.indexOf("px")>0)?options.height:(options.height+"px");
			}

			//设置args参数
			//artDialog.data("args",options.args);
			
			//打开弹出层
			//artDialog.open(options.url,options,false);
			//this.hideWord();
			var $body = $(top.document).find("body");
			var _index = $body.find(".dialog").size() + 1000 + 1;
			
			this._allOptions[_index] = options;
			
			$body.append("<div id='dlg_"+_index+"' class='modal modal-iframe dialog' style='z-index:"+_index+"'></div>")
			$body.find("#dlg_"+_index).addClass("active");
			$body.find("#dlg_"+_index).append("<header class='bar bar-nav'><a id='close' data-ignore='push' class='icon icon-close pull-right' href='#dlg_"+_index+"'></a><h1 class='title'>"+options.title+"</h1></header>");
			
			var closeObj = document.getElementById('close');
			closeObj.addEventListener('touchend', function(event) {
				OBPM.dialog.doExit();
				event.preventDefault();
			}, false);
			
			var ts = + new Date;
			if(!options.args.url){
				var ret = options.url.replace(/([?&])_=[^&]*/, "$1_=" + ts );
					url = ret + ((ret === options.url) ? (/\?/.test(options.url) ? "&" : "?") + "_=" + ts : "");
			}else{
				var ret = options.args.url.replace(/([?&])_=[^&]*/, "$1_=" + ts );
					url = ret + ((ret === options.args.url) ? (/\?/.test(options.args.url) ? "&" : "?") + "_=" + ts : "");
			}
			$body.find("#dlg_"+_index).append("<div class='content' " +
					"style=''>" +
					"<iframe src='"+url+"' frameborder='0' allowtransparency='true' " +
							"style='position:absolute;z-index:1;top:0px;width: 100%; height: 100%; border: 0px none;'></iframe>" +
							"</div>");
			var pageYoffset = window.pageYOffset;
			
			$("#document_content").hide();
			$("#myModalexample").hide();
			$("#dlg_"+_index).find(".content").css({"position":"static","height":$(window).height()-44})
			$("#dlg_"+_index).find(".content>iframe").css({"position":"absolute","z-index":"1","top":"44px","height":$(window).height()-44})			
			
			$body.find("#dlg_"+_index).find(">div>iframe").load(function(){
				options._pageYOffset = pageYoffset;
				//window.scrollTo(0,0);
				});
		},
		/**
		 * 打开弹出层时隐藏处于显示状态的word控件
		 */
		hideWord : function(){
			//嵌入表单的word控件
			jQuery("iframe[type=word]").each(function(){
				if(jQuery(this).css("display")!="none"){
					jQuery(this).attr("artDialogHide","true").css("display","none");
				}
			});
			//包含元素中嵌入的word控件
			jQuery("iframe").contents().find("iframe[type=word]").each(function(){
				if(jQuery(this).css("display")!="none"){
					jQuery(this).attr("artDialogHide","true").css("display","none");
				}
			});
			//弹出层显示的word控件中点击印章管理等弹出层时
			jQuery("#editmain_right").attr("artDialogHide","true").css("display","none");
		},
		/**
		 * 设置返回值并关闭窗口
		 */	
		doReturn : function(result) {
			var $body = $(top.document).find("body");
			var _index = $body.find(".dialog").size() + 1000;
			var _options = this._allOptions[_index] ;
			this.doExit(result);
		},

		doExit : function(result) {
			var $body = $(top.document).find("body");
			var _index = $body.find(".dialog").size() + 1000;
			var _options = this._allOptions[_index] ;

			if (_options && _options.close) {
				var fn = _options.close;					
				if (typeof fn === 'function' && fn.call(this, result) === false) {
					return;
				};
			}
			$("#document_content").show();
			$("#myModalexample").show();
			window.scrollTo(0,_options._pageYOffset);
			var $dialog_box = $body.find("#dlg_"+_index);
			$dialog_box.removeClass("active");
			$dialog_box.remove();	
		},

		doClear : function(val) {
			if(!val){
				val = "";
			}
			var $body = $(top.document).find("body");
			var _index = $body.find(".dialog").size() + 1000;
			var _options = this._allOptions[_index] ;
			if (_options && _options.close) {
				var fn = _options.close;					
				if (typeof fn === 'function' && fn.call(this, val) === false) {
					return;
				};
			}
			
			this.doExit();
		},

		//doClearUpload : function() {// 清空文件上传
		//	var winArtDialog = artDialog.getOpenApi();
		//	winArtDialog.close('clear');
		//},

		/**
		 * 获取弹出框参数
		 */
		getArgs : function() {
			var $body = $(top.document).find("body");
			var _index = $body.find(".dialog").size() + 1000;
			var _options = this._allOptions[_index] ;
			return _options.args;
		},

		/**
		 * 调整弹出框高度宽度, 小于等于-1表示不更改
		 */
		resize : function(width, height) {
		}
	};
})(jQuery);; 
; 
/**
 * phone 皮肤弹出层组件、在页面上已单例的形式存在
 */
var MyPopup = MyPopup || {
	
	_initialized : false,
	
	_modal : undefined,
	
	_callback : undefined,
	
	init : function(){
		//MyPopup.cache.clear();
		this._modal = $("#myModalexample");
		
		$("#myModalexample").on("popup",function(e){
			alert('popup');
		});

		$("#myModalexample").on("close",function(e){
			$(this).removeClass("active");
			if(MyPopup._callback && typeof MyPopup._callback == "function"){
				MyPopup._callback();
			}
		});
		
		
		
	},
		
	open : function(settings){
		settings = settings || {};
		var url = settings.url;
		var title = settings.title;
		var successCallback = settings.success;
		var closeCallback = settings.close;
		var options = settings.options || {};
		MyPopup._callback = successCallback;
		//MyPopup.cache.set("success",successCallback);
		$("#myModalexample .title").html(title);
		$("#myModalexample").find("iframe").attr("src",url);
		$("#myModalexample").addClass("active");
	},
	
	MyPopupClose : function(){
		Activity.closeWindow();
	},
	
};

/**
 * 缓存
 */
MyPopup.cache = {
	
		get:function(key){
			var top = window.top, 
			cache = top['_CACHE'] || {}; 
			top['_CACHE'] = cache;
			return cache[key];
		},
		
		set:function(key,value){
			var top = window.top, 
			cache = top['_CACHE'] || {}; 
			top['_CACHE'] = cache;
			cache[key]=value;
		},
		clear:function(key){
			var top = window.top, 
			cache = top['_CACHE'] || {}; 
			top['_CACHE'] = cache;
			if(cache[key]) delete cache[key];
			if(!key) top['_CACHE'] = {};
		}
};

if(!MyPopup._initialized){
	$(document).ready(function(){
		$("body").append('<div id="myModalexample" class="modal modal-iframe">'+
				  '<header class="bar bar-nav">'+
				    '<a class="icon icon-close pull-right" id="btn-modal-close" onclick="MyPopup.MyPopupClose()"></a>'+
				    '<h1 class="title">创建</h1>'+
				  '</header>'+
				  '<div class="content">'+
				    '<iframe src="about:blank" width="100%" height="100%"></iframe>'+
				  '</div>'+
				'</div>');
		MyPopup.init();
		MyPopup._initialized = true;
	});
	
}; 
(function () {
    'use strict';
    var definePinchZoom = function ($) {
    	
        var PinchZoom = function (el, options) {
                this.el = $(el);
                this.zoomFactor = 1;
                this.lastScale = 1;
                this.offset = {
                    x: 0,
                    y: 0
                };
                this.options = $.extend({}, this.defaults, options);
                this.setupMarkup();
                this.bindEvents();
                this.update();
                // default enable.
                this.enable();

            },
            sum = function (a, b) {
                return a + b;
            },
            isCloseTo = function (value, expected) {
                return value > expected - 0.01 && value < expected + 0.01;
            };

        PinchZoom.prototype = {

            defaults: {
                tapZoomFactor: 2,
                zoomOutFactor: 1.3,
                animationDuration: 300,
                animationInterval: 5,
                maxZoom: 4,
                minZoom: 0.5,
                lockDragAxis: false,
                use2d: true,
                zoomStartEventName: 'pz_zoomstart',
                zoomEndEventName: 'pz_zoomend',
                dragStartEventName: 'pz_dragstart',
                dragEndEventName: 'pz_dragend',
                doubleTapEventName: 'pz_doubletap'
            },

            /**
             * Event handler for 'dragstart'
             * @param event
             */
            handleDragStart: function (event) {
                this.el.trigger(this.options.dragStartEventName);
                this.stopAnimation();
                this.lastDragPosition = false;
                this.hasInteraction = true;
                this.handleDrag(event);
            },

            /**
             * Event handler for 'drag'
             * @param event
             */
            handleDrag: function (event) {

                if (this.zoomFactor > 1.0) {
                    var touch = this.getTouches(event)[0];
                    this.drag(touch, this.lastDragPosition);
                    this.offset = this.sanitizeOffset(this.offset);
                    this.lastDragPosition = touch;
                }
            },

            handleDragEnd: function () {
                this.el.trigger(this.options.dragEndEventName);
                this.end();
            },

            /**
             * Event handler for 'zoomstart'
             * @param event
             */
            handleZoomStart: function (event) {
                this.el.trigger(this.options.zoomStartEventName);
                this.stopAnimation();
                this.lastScale = 1;
                this.nthZoom = 0;
                this.lastZoomCenter = false;
                this.hasInteraction = true;
            },

            /**
             * Event handler for 'zoom'
             * @param event
             */
            handleZoom: function (event, newScale) {

                // a relative scale factor is used
                var touchCenter = this.getTouchCenter(this.getTouches(event)),
                    scale = newScale / this.lastScale;
                this.lastScale = newScale;

                // the first touch events are thrown away since they are not precise
                this.nthZoom += 1;
                if (this.nthZoom > 3) {

                    this.scale(scale, touchCenter);
                    this.drag(touchCenter, this.lastZoomCenter);
                }
                this.lastZoomCenter = touchCenter;
            },

            handleZoomEnd: function () {
                this.el.trigger(this.options.zoomEndEventName);
                this.end();
            },

            /**
             * Event handler for 'doubletap'
             * @param event
             */
            handleDoubleTap: function (event) {
                var center = this.getTouches(event)[0],
                    zoomFactor = this.zoomFactor > 1 ? 1 : this.options.tapZoomFactor,
                    startZoomFactor = this.zoomFactor,
                    updateProgress = (function (progress) {
                        this.scaleTo(startZoomFactor + progress * (zoomFactor - startZoomFactor), center);
                    }).bind(this);

                if (this.hasInteraction) {
                    return;
                }
                if (startZoomFactor > zoomFactor) {
                    center = this.getCurrentZoomCenter();
                }

                this.animate(this.options.animationDuration, this.options.animationInterval, updateProgress, this.swing);
                this.el.trigger(this.options.doubleTapEventName);
            },

            /**
             * Max / min values for the offset
             * @param offset
             * @return {Object} the sanitized offset
             */
            sanitizeOffset: function (offset) {
                var maxX = (this.zoomFactor - 1) * this.getContainerX(),
                    maxY = (this.zoomFactor - 1) * this.getContainerY(),
                    maxOffsetX = Math.max(maxX, 0),
                    maxOffsetY = Math.max(maxY, 0),
                    minOffsetX = Math.min(maxX, 0),
                    minOffsetY = Math.min(maxY, 0);

                return {
                    x: Math.min(Math.max(offset.x, minOffsetX), maxOffsetX),
                    y: Math.min(Math.max(offset.y, minOffsetY), maxOffsetY)
                };
            },

            /**
             * Scale to a specific zoom factor (not relative)
             * @param zoomFactor
             * @param center
             */
            scaleTo: function (zoomFactor, center) {
                this.scale(zoomFactor / this.zoomFactor, center);
            },

            /**
             * Scales the element from specified center
             * @param scale
             * @param center
             */
            scale: function (scale, center) {
                scale = this.scaleZoomFactor(scale);
                this.addOffset({
                    x: (scale - 1) * (center.x + this.offset.x),
                    y: (scale - 1) * (center.y + this.offset.y)
                });
            },

            /**
             * Scales the zoom factor relative to current state
             * @param scale
             * @return the actual scale (can differ because of max min zoom factor)
             */
            scaleZoomFactor: function (scale) {
                var originalZoomFactor = this.zoomFactor;
                this.zoomFactor *= scale;
                this.zoomFactor = Math.min(this.options.maxZoom, Math.max(this.zoomFactor, this.options.minZoom));
                return this.zoomFactor / originalZoomFactor;
            },

            /**
             * Drags the element
             * @param center
             * @param lastCenter
             */
            drag: function (center, lastCenter) {
                if (lastCenter) {
                  if(this.options.lockDragAxis) {
                    // lock scroll to position that was changed the most
                    if(Math.abs(center.x - lastCenter.x) > Math.abs(center.y - lastCenter.y)) {
                      this.addOffset({
                        x: -(center.x - lastCenter.x),
                        y: 0
                      });
                    }
                    else {
                      this.addOffset({
                        y: -(center.y - lastCenter.y),
                        x: 0
                      });
                    }
                  }
                  else {
                    this.addOffset({
                      y: -(center.y - lastCenter.y),
                      x: -(center.x - lastCenter.x)
                    });
                  }
                }
            },

            /**
             * Calculates the touch center of multiple touches
             * @param touches
             * @return {Object}
             */
            getTouchCenter: function (touches) {
                return this.getVectorAvg(touches);
            },

            /**
             * Calculates the average of multiple vectors (x, y values)
             */
            getVectorAvg: function (vectors) {
                return {
                    x: vectors.map(function (v) { return v.x; }).reduce(sum) / vectors.length,
                    y: vectors.map(function (v) { return v.y; }).reduce(sum) / vectors.length
                };
            },

            /**
             * Adds an offset
             * @param offset the offset to add
             * @return return true when the offset change was accepted
             */
            addOffset: function (offset) {
                this.offset = {
                    x: this.offset.x + offset.x,
                    y: this.offset.y + offset.y
                };
            },

            sanitize: function () {
                if (this.zoomFactor < this.options.zoomOutFactor) {
                    this.zoomOutAnimation();
                } else if (this.isInsaneOffset(this.offset)) {
                    this.sanitizeOffsetAnimation();
                }
            },

            /**
             * Checks if the offset is ok with the current zoom factor
             * @param offset
             * @return {Boolean}
             */
            isInsaneOffset: function (offset) {
                var sanitizedOffset = this.sanitizeOffset(offset);
                return sanitizedOffset.x !== offset.x ||
                    sanitizedOffset.y !== offset.y;
            },

            /**
             * Creates an animation moving to a sane offset
             */
            sanitizeOffsetAnimation: function () {
                var targetOffset = this.sanitizeOffset(this.offset),
                    startOffset = {
                        x: this.offset.x,
                        y: this.offset.y
                    },
                    updateProgress = (function (progress) {
                        this.offset.x = startOffset.x + progress * (targetOffset.x - startOffset.x);
                        this.offset.y = startOffset.y + progress * (targetOffset.y - startOffset.y);
                        this.update();
                    }).bind(this);

                this.animate(
                    this.options.animationDuration,
                    this.options.animationInterval,
                    updateProgress,
                    this.swing
                );
            },

            /**
             * Zooms back to the original position,
             * (no offset and zoom factor 1)
             */
            zoomOutAnimation: function () {
                var startZoomFactor = this.zoomFactor,
                    zoomFactor = 1,
                    center = this.getCurrentZoomCenter(),
                    updateProgress = (function (progress) {
                        this.scaleTo(startZoomFactor + progress * (zoomFactor - startZoomFactor), center);
                    }).bind(this);

                this.animate(
                    this.options.animationDuration,
                    this.options.animationInterval,
                    updateProgress,
                    this.swing
                );
            },

            /**
             * Updates the aspect ratio
             */
            updateAspectRatio: function () {
                this.setContainerY(this.getContainerX() / this.getAspectRatio());
            },

            /**
             * Calculates the initial zoom factor (for the element to fit into the container)
             * @return the initial zoom factor
             */
            getInitialZoomFactor: function () {
                // use .offsetWidth instead of width()
                // because jQuery-width() return the original width but Zepto-width() will calculate width with transform.
                // the same as .height()
                return this.container[0].offsetWidth / this.el[0].offsetWidth;
            },

            /**
             * Calculates the aspect ratio of the element
             * @return the aspect ratio
             */
            getAspectRatio: function () {
                return this.el[0].offsetWidth / this.el[0].offsetHeight;
            },

            /**
             * Calculates the virtual zoom center for the current offset and zoom factor
             * (used for reverse zoom)
             * @return {Object} the current zoom center
             */
            getCurrentZoomCenter: function () {

                // uses following formula to calculate the zoom center x value
                // offset_left / offset_right = zoomcenter_x / (container_x - zoomcenter_x)
                var length = this.container[0].offsetWidth * this.zoomFactor,
                    offsetLeft  = this.offset.x,
                    offsetRight = length - offsetLeft -this.container[0].offsetWidth,
                    widthOffsetRatio = offsetLeft / offsetRight,
                    centerX = widthOffsetRatio * this.container[0].offsetWidth / (widthOffsetRatio + 1),

                // the same for the zoomcenter y
                    height = this.container[0].offsetHeight * this.zoomFactor,
                    offsetTop  = this.offset.y,
                    offsetBottom = height - offsetTop - this.container[0].offsetHeight,
                    heightOffsetRatio = offsetTop / offsetBottom,
                    centerY = heightOffsetRatio * this.container[0].offsetHeight / (heightOffsetRatio + 1);

                // prevents division by zero
                if (offsetRight === 0) { centerX = this.container[0].offsetWidth; }
                if (offsetBottom === 0) { centerY = this.container[0].offsetHeight; }

                return {
                    x: centerX,
                    y: centerY
                };
            },

            canDrag: function () {
                return !isCloseTo(this.zoomFactor, 1);
            },

            /**
             * Returns the touches of an event relative to the container offset
             * @param event
             * @return array touches
             */
            getTouches: function (event) {
                var position = this.container.offset();
                return Array.prototype.slice.call(event.touches).map(function (touch) {
                    return {
                        x: touch.pageX - position.left,
                        y: touch.pageY - position.top
                    };
                });
            },

            /**
             * Animation loop
             * does not support simultaneous animations
             * @param duration
             * @param interval
             * @param framefn
             * @param timefn
             * @param callback
             */
            animate: function (duration, interval, framefn, timefn, callback) {
                var startTime = new Date().getTime(),
                    renderFrame = (function () {
                        if (!this.inAnimation) { return; }
                        var frameTime = new Date().getTime() - startTime,
                            progress = frameTime / duration;
                        if (frameTime >= duration) {
                            framefn(1);
                            if (callback) {
                                callback();
                            }
                            this.update();
                            this.stopAnimation();
                            this.update();
                        } else {
                            if (timefn) {
                                progress = timefn(progress);
                            }
                            framefn(progress);
                            this.update();
                            setTimeout(renderFrame, interval);
                        }
                    }).bind(this);
                this.inAnimation = true;
                renderFrame();
            },

            /**
             * Stops the animation
             */
            stopAnimation: function () {
                this.inAnimation = false;
            },

            /**
             * Swing timing function for animations
             * @param p
             * @return {Number}
             */
            swing: function (p) {
                return -Math.cos(p * Math.PI) / 2  + 0.5;
            },

            getContainerX: function () {
                return this.container[0].offsetWidth;
            },

            getContainerY: function () {
                return this.container[0].offsetHeight;
            },

            setContainerY: function (y) {
                return this.container.height(y);
            },

            /**
             * Creates the expected html structure
             */
            setupMarkup: function () {
                this.container = $('<div class="pinch-zoom-container"></div>');
                this.el.before(this.container);
                this.container.append(this.el);

                this.container.css({
                    'overflow': 'hidden',
                    'position': 'relative',
                    'width': '100%',
                	'height': '100%'
                });

                // Zepto doesn't recognize `webkitTransform..` style
                this.el.css({
                    '-webkit-transform-origin': '0% 0%',
                    '-moz-transform-origin': '0% 0%',
                    '-ms-transform-origin': '0% 0%',
                    '-o-transform-origin': '0% 0%',
                    'transform-origin': '0% 0%',
                    'position': 'absolute'
                });
            },

            end: function () {
                this.hasInteraction = false;
                this.sanitize();
                this.update();
            },

            /**
             * Binds all required event listeners
             */
            bindEvents: function () {
                detectGestures(this.container.get(0), this);
                // Zepto and jQuery both know about `on`
                $(window).on('resize', this.update.bind(this));
                $(this.el).find('img').on('load', this.update.bind(this));
            },

            /**
             * Updates the css values according to the current zoom factor and offset
             */
            update: function () {

                if (this.updatePlaned) {
                    return;
                }
                this.updatePlaned = true;

                setTimeout((function () {
                    this.updatePlaned = false;
                    this.updateAspectRatio();

                    var zoomFactor = this.getInitialZoomFactor() * this.zoomFactor,
                        offsetX = -this.offset.x / zoomFactor,
                        offsetY = -this.offset.y / zoomFactor,
                        transform3d =   'scale3d('     + zoomFactor + ', '  + zoomFactor + ',1) ' +
                            'translate3d(' + offsetX    + 'px,' + offsetY    + 'px,0px)',
                        transform2d =   'scale('       + zoomFactor + ', '  + zoomFactor + ') ' +
                            'translate('   + offsetX    + 'px,' + offsetY    + 'px)',
                        removeClone = (function () {
                            if (this.clone) {
                                this.clone.remove();
                                delete this.clone;
                            }
                        }).bind(this);

                    // Scale 3d and translate3d are faster (at least on ios)
                    // but they also reduce the quality.
                    // PinchZoom uses the 3d transformations during interactions
                    // after interactions it falls back to 2d transformations
                    if (!this.options.use2d || this.hasInteraction || this.inAnimation) {
                        this.is3d = true;
                        removeClone();
                        this.el.css({
                            '-webkit-transform':  transform3d,
                            '-o-transform':       transform2d,
                            '-ms-transform':      transform2d,
                            '-moz-transform':     transform2d,
                            'transform':        transform3d
                        });
                    } else {

                        // When changing from 3d to 2d transform webkit has some glitches.
                        // To avoid this, a copy of the 3d transformed element is displayed in the
                        // foreground while the element is converted from 3d to 2d transform
                        if (this.is3d) {
                            this.clone = this.el.clone();
                            this.clone.css('pointer-events', 'none');
                            this.clone.appendTo(this.container);
                            setTimeout(removeClone, 200);
                        }
                        this.el.css({
                            '-webkit-transform':  transform2d,
                            '-o-transform':       transform2d,
                            '-ms-transform':      transform2d,
                            '-moz-transform':     transform2d,
                            'transform':        transform2d
                        });
                        this.is3d = false;
                    }
                }).bind(this), 0);
            },

            /**
             * Enables event handling for gestures
             */
            enable: function() {
              this.enabled = true;
            },

            /**
             * Disables event handling for gestures
             */
            disable: function() {
              this.enabled = false;
            }
        };

        var detectGestures = function (el, target) {
            var interaction = null,
                fingers = 0,
                lastTouchStart = null,
                startTouches = null,

                setInteraction = function (newInteraction, event) {
                    if (interaction !== newInteraction) {

                        if (interaction && !newInteraction) {
                            switch (interaction) {
                                case "zoom":
                                    target.handleZoomEnd(event);
                                    break;
                                case 'drag':
                                    target.handleDragEnd(event);
                                    break;
                            }
                        }

                        switch (newInteraction) {
                            case 'zoom':
                                target.handleZoomStart(event);
                                break;
                            case 'drag':
                                target.handleDragStart(event);
                                break;
                        }
                    }
                    interaction = newInteraction;
                },

                updateInteraction = function (event) {
                    if (fingers === 2) {
                        setInteraction('zoom');
                    } else if (fingers === 1 && target.canDrag()) {
                        setInteraction('drag', event);
                    } else {
                        setInteraction(null, event);
                    }
                },

                targetTouches = function (touches) {
                    return Array.prototype.slice.call(touches).map(function (touch) {
                        return {
                            x: touch.pageX,
                            y: touch.pageY
                        };
                    });
                },

                getDistance = function (a, b) {
                    var x, y;
                    x = a.x - b.x;
                    y = a.y - b.y;
                    return Math.sqrt(x * x + y * y);
                },

                calculateScale = function (startTouches, endTouches) {
                    var startDistance = getDistance(startTouches[0], startTouches[1]),
                        endDistance = getDistance(endTouches[0], endTouches[1]);
                    return endDistance / startDistance;
                },

                cancelEvent = function (event) {
                    event.stopPropagation();
                    event.preventDefault();
                },

                detectDoubleTap = function (event) {
                    var time = (new Date()).getTime();

                    if (fingers > 1) {
                        lastTouchStart = null;
                    }

                    if (time - lastTouchStart < 300) {
                        cancelEvent(event);

                        target.handleDoubleTap(event);
                        switch (interaction) {
                            case "zoom":
                                target.handleZoomEnd(event);
                                break;
                            case 'drag':
                                target.handleDragEnd(event);
                                break;
                        }
                    }

                    if (fingers === 1) {
                        lastTouchStart = time;
                    }
                },
                firstMove = true;

            el.addEventListener('touchstart', function (event) {
                if(target.enabled) {
                    firstMove = true;
                    fingers = event.touches.length;
                    detectDoubleTap(event);
                }
            });

            el.addEventListener('touchmove', function (event) {
                if(target.enabled) {
                    if (firstMove) {
                        updateInteraction(event);
                        if (interaction) {
                            cancelEvent(event);
                        }
                        startTouches = targetTouches(event.touches);
                    } else {
                        switch (interaction) {
                            case 'zoom':
                                target.handleZoom(event, calculateScale(startTouches, targetTouches(event.touches)));
                                break;
                            case 'drag':
                                target.handleDrag(event);
                                break;
                        }
                        if (interaction) {
                            cancelEvent(event);
                            target.update();
                        }
                    }

                    firstMove = false;
                }
            });

            el.addEventListener('touchend', function (event) {
                if(target.enabled) {
                    fingers = event.touches.length;
                    updateInteraction(event);
                }
            });
        };

        return PinchZoom;
    };

    if (typeof define !== 'undefined' && define.amd) {
        define(['jquery'], function ($) {
            return definePinchZoom($);
        });
    } else {
        window.RTP = window.RTP || {};
        window.RTP.PinchZoom = definePinchZoom(window.$);
    }
}).call(this);
; 
;
/**
 * 微信端图片上传控件
 * @author Happy
 * @param $
 */
(function($){
	
	
	/**
	 * 控件初始化
	 */
	function _init(t){
		
		var options = _parseOptions(t);
		
		t.after('<input type="hidden" name="' + options.name+ '" fieldtype="'+t.attr("fieldtype")+'" value=\'' + (options.value?options.value:'') + '\' id="' + options.id + '"/>');
		var panel= $('<div id="weixinImageUpload_container_'+options.id+'" class="formfield-wrap"><label class="field-title contact-name" for="'+options.discription+'">'+options.discription+'</label></div>');
		//render
		panel.append(_renderImages(options));
		
		//t.hide();
		//panel.insertAfter(t);
//		panel.find("input[name='"+options.name+"']").val(options.value?options.value:'');
		
		$("body").append('<div _pid="'+options.id+'" class="modal bigpic preview-panel"><div class="bigpicbox">'
						+'<div class="content">'
							+'<div class="lookImg"><img class="preview-item" src="" /></div>'
						+'</div>'
						+'<div class="bar bar-standard bar-footer">'
							+'<a class="btn pull-left btn-close">退出预览</a>'		  
								+'<a class="btn btn-negative pull-right btn-delete-popup" data-ignore="push">删除</a>'
						+'</div>'
					+'</div></div>');
		
		$("body").find(".bigpic[_pid='"+options.id+"']").find(".bigpicbox").append('<div class="tanchu">'
						+'<div class="header">提示</div>'
						+'<div class="contenter">'
							+'<p>确认删除当前？</p>'
						+'</div>'
						+'<div class="foot">'
							+'<a class="btn pull-right  btn-link red btn-delete" data-ignore="push">删除</a>'
							+'<a class="btn pull-right btn-link gay btn-cancel">取消</a>'
						+'</div>'
					+'</div>');
		
		if(options.readonly){
			panel.find(".btn-delete-popup").hide();
		}
		
		// 描述
		if(t.attr("moduleType") != "uploadImageRefresh"){
			t.attr("moduleType", "uploadImageRefresh").css("display","none");
		}
		
		panel.insertAfter(t);
		
		return panel;
	}
	
	function _renderImages(options){
		var imagesDiv = $('<div class="card_app smallpic image-list" style="margin-top:0px;"></div>');
		var images = options.value? JSON.parse(options.value):[];
		for(var i=0;i<images.length;i++){
			imagesDiv.append('<a data-ignore="push" class="image-item" data-name="'+images[i].name+'" data-path="'+images[i].path+'" ><img src="'+contextPath+images[i].path+'"/></a>');
		}
		if(!options.readonly){
			imagesDiv.append('<a data-ignore="push" class="btn-upload"><span class="icon icon-plus"></span></a>');
		}

		return imagesDiv;
	}
	
	
	/**
	 * 事件绑定
	 */
	function _bindEvents(t){
		var panel = t.data("weixinImageUpload").panel;
		var $bigView = $("body").find(".preview-panel[_pid='"+t.attr("id")+"']")
			
		panel.find(".btn-upload").on("click",function(){
			_chooseImage(t);
		});
		
		panel.find(".image-item").on("click",function(){
			var pid = $(this).parent().siblings("input").attr("id");
			var src = $(this).find("img").attr("src");
			var name = $(this).data("name");
			$bigView.find(".preview-item").attr("src",src);
			$bigView.find(".preview-item").data("name",name);
			$bigView.addClass("active");
			$("div.lookImg").each(function () {
  				new RTP.PinchZoom($(this), {});
  			});
		});
		
		$bigView.find(".btn-close").on("click",function(){
			$bigView.removeClass("active");
		});
		
		$bigView.find(".btn-delete-popup").on("click",function(){
			$bigView.find(".tanchu").addClass("block animated bounceIn");
		});
		
		$bigView.find(".btn-cancel").on("click",function(){
			$bigView.find(".tanchu").removeClass("block animated bounceIn");
		});
		$bigView.find(".btn-delete").on("click",function(){
			var name = $bigView.find(".preview-item").data("name");
			_removeImage(t,name);
			$bigView.find(".tanchu").removeClass("block animated bounceIn");
			$bigView.removeClass("active");
		});
	}
	
	function _onImageItemClick(item){
		var src = item.find("img").attr("src");
		var name = item.data("name");
		panel.find(".preview-item").attr("src",src);
		panel.find(".preview-item").data("name",name);
		panel.find(".preview-panel").addClass("active");
	}
	
	/**
	 * 解析控件参数
	 */
	function _parseOptions(t){
		var options = {};
		options.id = t.attr("id");
		options.name = t.attr("name");
		options.discription = HTMLDencode(t.attr("discript"))? HTMLDencode(t.attr("discript")):options.name;
		options.value = t.attr("value");
		options.limitnumber = t.attr("limitNumber");
		options.maxsize = t.attr("maxsize");
		options.refreshOnChanged = t.attr("refreshOnChanged");
		options.readonly = (t.attr("disabled") == 'disabled');
		return options;
	}
	
	
	function _chooseImage(t){
		var panel = t.data("weixinImageUpload").panel;
		var _wx = top.wx ? top.wx : wx;
		 _wx.chooseImage({
			 count: 9, 
			 sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
			 sourceType: ['album','camera'], // 可以指定来源是相册还是相机，默认二者都有
		     success: function (res) {
		        var localIds = res.localIds;
		        setTimeout(function(){
		        	_uploadImage(t,panel,_wx,localIds);
		        }, 200);
		      }
		    });
	}
	
	function _uploadImage(t,panel,_wx,localIds){
		var localId = localIds.pop();
		 _wx.uploadImage({
		        localId: localId,
		        isShowProgressTips: 1,// 默认为1，显示进度提示
		        success: function (res) {
		          var serverId = res.serverId;
		          $.ajax({
		        	  url:contextPath+"/portal/weixin/jsapi/upload.action",
		        	  async:false,
		        	  type:"get",
		        	  data:{"serverId":serverId},
		        	  dataType:"json",
		        	  success:function(result){
				          if(result.status==1){
				        	  var path = result.data;
				        	  var name = result.data.substring(result.data.lastIndexOf("/")+1,result.data.length);
				        	  var imageItem = $('<a data-ignore="push" class="image-item" data-name="'+name+'" data-path="'+path+'" ><img src="'+localId+'"/></a>');
				        	  imageItem.on("click",function(){
				        		  	var src = $(this).find("img").attr("src");
				      				var name = $(this).data("name");
					      			panel.find(".preview-item").attr("src",src);
					      			panel.find(".preview-item").data("name",name);
					      			panel.find(".preview-panel").addClass("active");
				        	  });
				        	  panel.find(".btn-upload").before(imageItem);
				        	  try {
				        		  _addImage(t,{name:name,path:path});
				        	  } catch (e) {
				        		  alert(e.stack.toString());
				        	  }
				        	  if(localIds.length>0){
				        		  _uploadImage(t,panel,_wx,localIds);
				        	  }
						      _bindEvents(t);
				          }
				       }
		          });
		        },
		        fail: function (res) {
		          alert("网络异常，请再次尝试！");
		        }
		      });
	}
	
	function _addImage(t,data){
		var options = t.data("weixinImageUpload").options;
		var vf = $("#"+options.id);
		var json = vf.val();
		var images = json? JSON.parse(json):[];
		images.push(data);
		vf.val(JSON.stringify(images));
	}
	
	function _removeImage(t,name){
		var state = t.data("weixinImageUpload");
		var panel =state.panel;
		var options = state.options;
		panel.find(".image-item").each(function(){
			var $this = $(this);
			if($this.data("name")==name){
				$this.remove();
				var vf = $("#"+options.id);
				var images = JSON.parse(vf.val());
				for(var i=0;i<images.length;i++){
					if(images[i].name==name){
						images.splice(i,1);
						vf.val(JSON.stringify(images));
					}
					break;
				}
				return;
			}
		});
	}
	
	
	
	$.fn.obpmWeixinImageUpload = function(options, param){
		
		if(typeof options=="string"){
			return $.fn.obpmWeixinImageUpload.method[options](this,param);
		}
		options = options || {};
		return this.each(function(){
			var t = $(this);
			var state = t.data("weixinImageUpload");
			if(state){
				$.extend(state.options,options);
			}else{
				var r =_init(t);
				state = t.data('weixinImageUpload', {
					options: $.extend({}, $.fn.obpmWeixinImageUpload.defaults, _parseOptions(t), options),
					panel: r
				});
				_bindEvents(t);
			}
		});
	},
	
	$.fn.obpmWeixinImageUpload.defaults = {
			id:'',
			name:'',
			value:null,
			discription:'',
			limitnumber:null,
			maxsize:10240,
			path:"ITEM_PATH",
			refreshOnChanged:null,
			readonly:false
			
	},
	
	$.fn.obpmWeixinImageUpload.method= {
			
		setValue:function(jq,value){
			
		},
		getValue:function(jq){
			jq.each(function(){
				//nothing
			});
		}
	}
	
})(jQuery);
; 
;
/**
 * 微信录音控件
 * @author Happy
 * @param $
 */
(function($){
	
	var $bigRecBox="";
	
	/**
	 * 显示类型. 分别：1.只读(READONLY)2.修改(MODIFY),3.隐藏(HIDDEN),4.禁用(DISABLED)
	 */
	var DisplayType = {
			READONLY:1,//只读
			MODIFY:2,//修改
			HIDDEN:3,//隐藏
			DISABLED:4 //禁用
			

	};
	
	/**
	 * 控件初始化
	 */
	function _init(t){
		var options = _parseOptions(t);
		var panel = $('<div id="weixinRecord_container_'+options.id+'"></div>');
		var content = $('<div></div>');
		content.append(' <div class="formfield-wrap record-panel" style="display:none;">'
			       +' <div class="record-box">'
			       +'   <a class="btn btn-block btn-record startRecord" >我要说话</a>'
			       +' </div>'
			       +'</div>');
		
		content.append('<div class="formfield-wrap player-panel" style="display:none;margin-bottom:10px;">'
				+' <div class="record-box">'
				+'  <a class="btn-sound-play">'
				+'    <div class="sound-play-arrow"></div>'
				+'    <div class="sound-play-box">'
				+'      <div class="play-ico sound-play-ico sound-stop"></div>'
				+'    </div>'
				+' </a>'
				+'  <a class="btn-sound-space"></a>'
				+'  <a class="btn-sound-delete">'
				+'    <span class="icon icon-close"></span>'
				+'  </a>'
				+'  <div class="sound-delete-box">'
				+'      <div class="header">提示</div>'
				+'      <div class="contenter"><p>确认删除当前？</p></div>'
				+'      <div class="foot">'
				+'        <a class="btn pull-right  btn-link red btn-delete" data-ignore="push">删除</a>'
				+'        <a class="btn pull-right btn-link gay btn-cancel">取消</a>'
				+'      </div>'
				+'    </div>'
				+' </div>'
				+'</div>');
		
		$(document).find("#div_button_box").before("<div id='wxRec_" + options.id + "' class='bigrec-box' style='display:none'></div>");
		$bigRecBox = $("body").find("#wxRec_" + options.id);
		
		$bigRecBox.append('<div class="modal modal-iframe recording-panel">'
		       +'     <div class="record-play-box">'
		       +'       <div class="record-play-ico">'
		       +'         <div class="sound-box"></div>' 
		       +'       </div>'
		       +'       <div class="sound-text"><p>正在录音</p><p class="time-total">0</p></div>'
		       +'       <div class="btn-record-stop text-center">'
		       +'         <span class="icon icon-stop stopRecord"></span>'
		       +'         <p>停止</p>'
		       +'       </div>'
		       +' </div>'
		       +'</div>');

		if(options.value && options.value.length>0){
			var voice = JSON.parse(options.value);
			var vioceItem = $('<audio><source src="'+contextPath+voice.path+'" type="audio/mpeg" /></audio>');
			panel.append(vioceItem);
			content.find(".player-panel").show();
			if(options.displayType ==DisplayType.READONLY || options.displayType ==DisplayType.DISABLED){
				content.find(".btn-sound-delete").hide();
			}
		}else if(options.displayType ==DisplayType.MODIFY){
				content.find(".record-panel").show();
			}
			
		
		panel.append(content);
		panel.insertAfter(t);
		
		return panel;
	}
	
	
	/**
	 * 事件绑定
	 */
	function _bindEvents(t){
		var panel = t.data("weixinRecord").panel;
		panel.find(".startRecord").on("click",function(e){
			$bigRecBox.show();
			setTimeout(function(){
				$bigRecBox.find(".recording-panel").addClass("active");
			},100);
			$bigRecBox.find(".record-play-ico").addClass("animate-play");
			var timer = _getTimer(t);
			_startRecord(t);
		});
		
		$bigRecBox.find(".stopRecord").on("click",function(e){
			$bigRecBox.find(".recording-panel").removeClass("active");
			setTimeout(function(){
				$bigRecBox.hide();
			},1000);
			var timer = _getTimer(t);
			clearInterval(timer);
			t.data("timer",null);
			_stopRecord(t);
		});
		
		panel.find(".sound-play-box").on("click",function(e){
			_playVoice(t);
		});
		
		panel.find(".btn-sound-delete").on("click",function(e){
			panel.find(".sound-delete-box").show().addClass("animated bounceIn");
		});
		panel.find(".btn-delete").on("click",function(e){
			_removeRecord(t);
		});
		panel.find(".btn-cancel").on("click",function(e){
			panel.find(".sound-delete-box").hide();
		});
		
	}
	
	/**
	 * 解析控件参数
	 */
	function _parseOptions(t){
		var options = {};
		options.id = t.attr("id");
		options.name = t.attr("name");
		options.discription = HTMLDencode(t.data("discription"))? HTMLDencode(t.data("discription")):options.name;
		options.value = t.val();
		options.displayType = t.data("displayType");
		return options;
	}
	
	/**
	 *	开始微信录音
	 */
	function _startRecord(t){
		var _wx = top.wx ? top.wx : wx;
		_wx.startRecord();
	}
	/**
	 *	停止微信录音
	 */
	function _stopRecord(t){
		var panel = t.data("weixinRecord").panel;
		var _wx = top.wx ? top.wx : wx;
		
		_wx.stopRecord({
		    success: function (res) {
		        var localId = res.localId;
		        _wx.uploadVoice({
		            localId: localId, // 需要上传的音频的本地ID，由stopRecord接口获得
		            isShowProgressTips: 1,// 默认为1，显示进度提示
		                success: function (res) {
		                var serverId = res.serverId; // 返回音频的服务器端ID
		                $.ajax({
				        	  url:contextPath+"/portal/weixin/jsapi/upload.action",
				        	  async:false,
				        	  type:"get",
				        	  data:{"serverId":serverId,"folder":"voice","fileType":"amr"},
				        	  dataType:"json",
				        	  success:function(result){
						          if(result.status==1){
						        	  var path = result.data.replace(".amr",".mp3");
						        	  var name = result.data.substring(result.data.lastIndexOf("/")+1,result.data.length);
						        	  var vioceItem = $('<audio><source src="'+contextPath+path+'" type="audio/mpeg" /></audio>');
						        	  t.data("voice",{"localId":localId,"path":path});
						        	 panel.append(vioceItem);
						        	 _addRecord(t,{"path":path,"count":$bigRecBox.find(".time-total").text()});
						        	 $bigRecBox.find(".time-total").text(0);
						          }
						       }
				          });
		            },
		            fail: function (res) {
				          alert("网络异常，请再次尝试！");
			        }
		        });
		    }
		});
	}
	/**
	 * 播放录音
	 */
	function _playVoice(t){
		var panel = t.data("weixinRecord").panel;
		panel.find(".play-ico").removeClass("sound-stop").addClass("sound-play");
		var voice = t.data("voice");
		var count = JSON.parse(t.val()).count;
		if(voice){
			var _wx = top.wx ? top.wx : wx;
			_wx.playVoice({
			    localId: voice.localId // 需要播放的音频的本地ID，由stopRecord接口获得
			});
		}else{
			var panel = t.data("weixinRecord").panel;
			panel.find("audio")[0].play();
			
		}
		setTimeout(function(){
			panel.find(".play-ico").removeClass("sound-play").addClass("sound-stop");
		},count*1000)
	}
	/**
	 * 暂停播放录音
	 */
	function _stopVoice(t){
		var panel = t.data("weixinRecord").panel;
		panel.find(".play-ico").removeClass("sound-play").addClass("sound-stop");
		var voice = t.data("voice");
		if(voice && false){
			var _wx = top.wx ? top.wx : wx;
			_wx.stopVoice({
			    localId: voice.localId // 需要播放的音频的本地ID，由stopRecord接口获得
			});
		}else{
			panel.find("audio")[0].pause();
		}
	}
	
	/**
	 * 添加录音记录
	 */
	function _addRecord(t,data){
		var state = t.data("weixinRecord");
		var options = state.options;
		var panel = state.panel;
		var vf = $("#"+options.id);
		vf.val(JSON.stringify(data));
		panel.find(".player-panel").show();
		panel.find(".record-panel").hide();
	}
	/**
	 * 删除录音记录
	 */
	function _removeRecord(t){
		var state = t.data("weixinRecord");
		var panel =state.panel;
		var options = state.options;
		var vf = $("#"+options.id);
		vf.val("");
		panel.find(".sound-delete-box").hide();
		panel.find(".player-panel").hide();
		panel.find(".record-panel").show();
	}
	
	/**
	 * 获取录音计时器
	 */
	function _getTimer(t){
		var panel = t.data("weixinRecord").panel;
		var timer = t.data("timer");
		if(!timer){
			timer = setInterval(function(){
						var tl = $bigRecBox.find(".time-total");
						tl.text(parseInt(tl.text())+1);
				},1000);
			t.data("timer",timer);
		}
		return timer;
	} 
	
	$.fn.obpmWeixinRecord = function(options, param){
		
		if(typeof options=="string"){
			return $.fn.obpmWeixinRecord.method[options](this,param);
		}
		options = options || {};
		return this.each(function(){
			var t = $(this);
			var state = t.data("weixinRecord");
			if(state){
				$.extend(state.options,options);
			}else{
				var r =_init(t);
				state = t.data('weixinRecord', {
					options: $.extend({}, $.fn.obpmWeixinRecord.defaults, _parseOptions(t), options),
					panel: r
				});
				_bindEvents(t);
			}
		});
	},
	
	$.fn.obpmWeixinRecord.defaults = {
			id:'',
			name:'',
			value:'',
			discription:'',
			displayType:null
	},
	
	$.fn.obpmWeixinRecord.method= {
			
		setValue:function(jq,value){
			
		},
		getValue:function(jq){
			jq.each(function(){
				//nothing
			});
		}
	}
	
})(jQuery);
; 
(function($) {
	$.fn.obpmButton = function() {
		return this
				.each(function() {
					var $btn = jQuery(this);
					var val = $btn.attr("value");
					var activityType = $btn.attr("activityType");

					// 29:批量签章;43:跳转
					// 嵌入视图不使用批量签章和跳转按钮
					if ((activityType == "29" || activityType == "43")
							&& typeof DisplayView == 'object'
							&& DisplayView.getTheView(this)) {
						$btn.remove();
						return;
					}
					/*
					 * activityType: 已完成： 新建:2; 删除:3; 提交:5; 返回:10; 保存并返回:11;
					 * 未完成： 查询Document:1; 保存并启动流程:4; SCRIPT处理:6; 关闭:8; 保存并关闭窗口:9;
					 * 返回:10; 保存并返回:11; 无:13; 打印:14; 连同流程图一起打印:15; 导出Excel:16; 保存并新建:17;
					 * 保存草稿不执行校验:19; 批量审批按钮:20; 保存并复制:21; 查看流程图:22; PDF导出:25;
					 * 文件下载:26; Excel导入:27; 电子签章:28; 批量电子签章:29; 跳转操作:32;
					 * 启动流程:33; 保存:34; 视图打印:36; 转发:37; Dispatcher:39; 保存并新建:42; 跳转:43;
					 * 归档:45; 不再使用： 修改Document:7; 关闭窗口:8;
					 * 保存并新建(新建有一条有旧数据Document):12; 清掉所有这个form的数据: 18;
					 * FLEX打印:30; FLEX带流程历史打印:31;
					 */

					switch (activityType) {
					case "1":
						activityCss = "btn-orange";
						break;
					case "2":
					case "4":
					case "42":
						activityCss = "btn-positive";
						break;
					case "3":
					case "8":
						activityCss = "btn-negative";
						break;
					case "5":
						activityCss = "";
						break;
					case "10":
						activityCss = "";
						break;
					case "11":
					case "34":
						activityCss = "btn-primary";
						break;
					default:
						activityCss = "";
						break;
					}

					if (activityType != "5"
							&& (activityType == 1 || activityType == 2 || activityType == 3
									|| activityType == 4
									|| activityType == 34
									|| activityType == 8
									|| activityType == 10
									|| activityType == 11 || activityType == 13
									|| activityType == 21 || activityType == 42 || activityType == 19)) {// 当按钮类型为5（流程处理）时，不做处理，具体渲染工作交由flowprcss.jsp完成

						var onclick = this.onclick;
						var $btnN = jQuery(
								"<td><a class='btn " + activityCss
										+ " btn-block' data-transition='fade'>"
										+ val + "</a></td>").click(onclick);

						$btnN.insertAfter($btn);
						$btn.remove();
					} else if (activityType != "5") {
						$btn.remove();
					}
				});
	};
})(jQuery);; 
/**
 * 按钮操作URL
 */
var activityAction = contextPath + "/portal/dynaform/activity/action.action";
var docviewAction = contextPath + '/portal/dynaform/document/view.action';

/**
 * 文件下载按钮对应的Function
 */
function doFileDonwload(actid) {
	if (actid) {
		var url = activityAction;
		var newUrl = url + '?_activityid=' + actid;
		
		if(ev_runbeforeScriptforview(actid)){
			document.forms[0].action = newUrl;
			document.forms[0].target = "_blank";
			document.forms[0].submit();
			document.forms[0].target = "";
		}
	}
}

/**
 * Excel导入按钮事件
 * 
 * @param {}
 *            actid 按钮ID
 * @param {}
 *            impcfgid 导入配置ID
 */
function ev_excelImport(actid, impcfgid) {
	var parentid = "";
	if (document.getElementsByName("parentid")) {
		parentid = document.getElementsByName("parentid")[0].value;
	}
	
	var application = "";
	if (document.getElementsByName("application")) {
		application = document.getElementsByName("application")[0].value;
	}

	var isRelate = "";
	if(document.getElementsByName("isRelate")){
		var relateObj = document.getElementsByName("isRelate");
		if(relateObj.length != 0)
			isRelate = relateObj[0].value;
	}
	var _viewid = document.getElementsByName("_viewid")[0].value;
	if (impcfgid) {
		var url = importURL; // importURL在各页面中定义
		url += "?parentid=" + parentid;
		url += "&id=" + impcfgid;
		url += "&application=" + application;
		url += "&isRelate=" + isRelate;
		url += "&_activityid=" + actid;
		url += "&_viewid=" + _viewid;
		OBPM.dialog.show({
			width : 530,
			height : 420,
			url : url,
			args : {},
			title : 'Excel导入',
			close : function(result) {
				document.forms[0].submit();
			}
		});
	}
}

/**
 * 打开下载窗口
 * 
 * @param {}
 *            fileName 文件名称
 */
function openDownloadWindow(fileName) {
	if (fileName) {
		var url = downloadURL + '?filename=' + encodeURI(fileName);
		top.window.open(url);
	}
}

/**
 * 页面重载
 */
function ev_reload() {
	// window.location.href = window.location.href+'&';
	document.forms[0].submit();
}

function ev_search() {
	$(document).find("form:eq(0)").find("[name='_currpage']").val("1");
	document.forms[0].submit();
}

/**
 * 清空字段内容
 */
function ev_resetAll() {
	var elements = document.forms[0].elements;
	for (var i = 0; i < elements.length; i++) {
		if(jQuery(elements[i]).attr('fieldType')=='TextAreaField'){
			elements[i].value = "";
		}
		if(jQuery(elements[i]).attr('fieldType')=='UserField'){
			elements[i].value = "";
		}
		if(jQuery(elements[i]).attr('fieldType')=='SelectAboutField'){
			var	originalName = elements[i].name;
			var	idDx = originalName + "ms2side__dx";
			var	idSx = originalName + "ms2side__sx";
			jQuery("#" + idDx).children().appendTo(jQuery("#" + idSx));
			jQuery("#" + idDx).children().remove();
			jQuery(elements[i]).find('option').attr("selected", false);
			jQuery(elements[i]).find("[text='']").attr("selected", true);
		}
		if(elements[i].type == 'checkbox'){
			elements[i].checked = false;
		}
		if(elements[i].type == 'radio'){
			elements[i].checked = false;
		}
		if(jQuery(elements[i]).attr('fieldType')=='TreeDepartmentField'){
			elements[i].value = "";
			elements[i+1].value = "";
		}
		// alert(elements[i].id + ": "+elements[i].type + " resetable-->" +
		// elements[i].resetable);
		if (elements[i].type == 'text'|| elements[i].resetable) {
			elements[i].value = "";
			
		} else if (elements[i].type == 'select-one') {
			// 还原至第一个选项
			if (elements[i].options.length >= 1) {
				elements[i].options[0].selected = true;
			}
		}
	}
	jQuery("#searchFormTable").find("input[type='hidden'][id!='dy_refreshObj']").val("");//清除隐藏文本框控件的值
	/*
	for (var i = 0; i < arrObject.length; i++) {
		arrObject[i].save("");
	}
	*/
}

/**
 * 表单提交(由按钮触发的事件)
 * 
 * @param {}
 *            activityId 操作ID
 * @param {}
 *            isCheckSelect 是否检查有选择项
 */
function ev_submit(activityId, isCheckSelect, operation,isConfirm,confirmMsg) {
	var flag = true;
	if (isCheckSelect) {
		flag = isSelectedOne("_selects");
	}
	if (flag) {
		if(ev_runbeforeScriptforview(activityId)){
			var formAction = contextPath + '/portal/dynaform/activity/handle.action' + '?_activityid=' + activityId;
			if (operation) {
				formAction += "&operation=" + operation;
			}
			if(isConfirm){
				var rtn = window.confirm(confirmMsg);
				if (!rtn){
					if(toggleButton("button_act")) return false;
					return;
				}
			}
			if(toggleButton("button_act")) return false;
			document.forms[0].action = formAction;
			document.forms[0].submit();
		}
	} else {
		if(toggleButton("button_act")) return false;
	}
	//hidden();
}

var ifExpToExl = true;
function ev_expToExl(activityId, filename, expSub){
	var oldActionUrl = document.forms[0].action;
	if(!ifExpToExl){
		alert("已导出，需要再次导出请刷新页面！");
		return;
	}

	if(ev_runbeforeScriptforview(activityId)){
		var url = contextPath + '/portal/dynaform/activity/handle.action' + '?_activityid=' + activityId + "&filename=" + filename + "&isExpSub=" + expSub;
		document.forms[0].action = url;
		document.forms[0].submit();
		ifExpToExl = false;
	}
	
	document.forms[0].action = oldActionUrl;
	
}

//批量提交
function doBatchApprove(activityId,isCheckSelect){
	if(!isSelectedOne("_selects","至少选择一条记录！")){
		return;
	}
	if(jQuery("#inputAuditRemarkDiv" + activityId).attr("id")){
		artDialog.prompt('请输入审批意见：',function(val,win){
			jQuery('#_attitude' + activityId).val(val);
			ev_submit(activityId,true);
		},true);
	}
}

// 键盘事件
function setEnter(event) {
	if (event.keyCode == 13) {
		event.keyCode = 9;
	}
}

function enterKeyDown(e) {
	if (e.keyCode == 13 || e.which == 13) { // 13 代表Enter
		document.getElementsByName('_currpage')[0].value = 1;
		document.forms[0].submit();
	}
}

//loading show
function dy_lock() {
	$("#loadingDivBack").show();
}

//loading hide
function dy_unlock() {
	setTimeout(function(){
		$("#loadingDivBack").hide();
	},1000);
}

function showLoadingToast() {
    $('#loadingToast').show();
}

function hideLoadingToast() {
    $('#loadingToast').hide();
}

function ev_exportToExcel(activityId, isConfirm, content){
	if(isConfirm){
		var rtn = window.confirm(content);
		if(!rtn){
			return;
		}
	}
	ev_submit(activityId);
}

/** 调整包含元素iframe高度--所有皮肤视图
 * iframeid: iframeid
 * viewType：1:列表视图(LISTVIEW),
 * 			 2:日历视图(CALENDARVIEW), 
 * 			 3:树形视图(TREEVIEW),
 * 			 4:地图视图(MAPVIEW),
 * 			 5:网格视图(GRIDVIEW),
 * 			 6:甘特视图(GANTTVIEW).
 */
function ev_resize4IncludeView(divid,iframeid,type){
	if(divid != "" && type != ""){
		var spanObj = parent.document.getElementById(divid);
		if(spanObj){
			var iframeObj = spanObj.getElementsByTagName("iframe");
			//jQuery("#" + divid + " iframe[id=" + iframeid + "]",parent.document)
			jQuery(iframeObj).each(function(){
				switch(type){
					case "MAPVIEW"://地图视图
						try{
							var activityH = jQuery("#activityTable").height();
							var searchH = 0;
							if(jQuery("#searchFormTable").size() != 0)
								searchH = jQuery("#searchFormTable").height();
							jQuery(this).css("height",(searchH + 670));
						}catch(ex){
							setTimeout(function(){
								jQuery(this).css("height",(searchH + 670));
							},300);
						}
						break;
						
					case "DISPLAYVIEW"://嵌入视图
						var formListH= document.getElementById('formList').offsetHeight;
						if(formListH > 410){
							formListH = 450;
						}else if(formListH < 100){
							formListH = 150;
						}else{
							formListH = formListH + 40;
						}
						//包含元素固定高度时不重新布局
						var fixation = jQuery(this).attr("fixation");
						if(fixation=="true"){
							jQuery(this).css("height",jQuery(this).attr("fixationHeight"));
						}else {
							jQuery(this).css("height",formListH);
						}
						break;
					case "CALENDARVIEW"://日历视图
						var actH = jQuery("#activityTable").height();
						var dataH= jQuery('#cal_viewcontent').height();
						var bodyH = actH + dataH;
						if(bodyH > 100){
							jQuery(this).css("height",(bodyH + 20));
						}
						break;
					case "TREEVIEW"://树形视图
						var bodyW = jQuery("body").width();
						jQuery(this).css("height",700);
						if(ie) jQuery(this).css("width",bodyW-4);
						break;
					case "GANTTVIEW"://甘特视图
						var iframeH = jQuery("#container").height();
						if(iframeH > 100)
							jQuery(this).css("height",(iframeH + 20));
						break;
					case "GRIDVIEW"://网格视图
						var acttableH = jQuery("#acttable").height();
						var dataTableH = jQuery("#gridTable").height();
						var formListH = acttableH + dataTableH;
						if(formListH > 380){
							formListH = 420;
						}else if(formListH < 100){
							formListH = 150;
						}else{
							formListH = formListH + 40;
						}
						jQuery(this).css("height",formListH);
						break;
					case "LISTVIEW"://列表视图
						break;
				}
			});
		}
	}
}

function ev_runbeforeScriptforview(actid){
	var flag = false;
	jQuery.ajax({
		type: 'POST',
		async:false, 
		url: contextPath + '/portal/dynaform/activity/runbeforeactionscript.action?_actid=' + actid,
		dataType : 'text',
		timeout: 3000,
		data: jQuery("#formList").serialize(),
		success:function(result) {
			if(result != null && result != ""){
	        	var jsmessage = eval("(" + result + ")");
	        	var type = jsmessage.type;
	        	var content = jsmessage.content;
	        	
	        	if(type){
	        		if(type == '16'){
		        		alert(content);
		        		flag = false;
		        	}
		        	
		        	if(type == '32'){
		        		var rtn = window.confirm(content);
		        		if(!rtn){
		        			flag = false;
		        		}else {
		        			flag = true;
		        		}
		        	}
	        	}else {
	        		flag = true;
	        	}
	        }else {
	        	flag = true;
	        }
		},
		error: function(result) {
			alert("运行脚本出错");
		}
	});
	
	return flag;
}

/** 当菜单视图显示记录数时，视图数据增删会刷新菜单记录数
 * for: default/dwz/fresh/gentle
 * viewType：1:列表视图(LISTVIEW),
 * 			 2:网格视图(GRIDVIEW).
 */
function refreshMenuTotalRows(){
	var menuId = "menu_" + jQuery("#resourceid").val();
	jQuery("body",parent.document).find("#" + menuId).html("(" + totalRows + ")");
}; 
/**
 * 窗口打开类型 为普通模式打开新的窗口
 */
var OPEN_TYPE_NORMAL = 0x0000001;
/**
 * 窗口打开类型 弹出框显示
 */
var OPEN_TYPE_POP = 0x0000010;
/**
 * 窗口打开类型 在父窗口区域显示
 */
var OPEN_TYPE_PARENT = 0x0000100;
/**
 * 窗口打开类型 当前区域显示
 */
var OPEN_TYPE_OWN = 0x0000110;
/**
 * 窗口打开类型 网格显示
 */
var OPEN_TYPE_GRID = 0x0000120;
/**
 * 窗口打开类型 弹出层显示
 */
var OPEN_TYPE_DIV = 0x0000115;
/**
 * 一般视图
 * 
 * @type Number
 */
var VIEW_TYPE_NORMAL = 0x0000010;
/**
 * 子视图
 * 
 * @type Number
 */
var VIEW_TYPE_SUB = 0x0000011;

var activityAction = contextPath + '/portal/dynaform/activity/action.action';
var importURL = contextPath
		+ '/portal/share/dynaform/dts/excelimport/importbyid.jsp';
var downloadURL = contextPath + '/portal/share/download.jsp'; // Excel下载URL

/**
 * 文档新建 (DocumentCreate)
 * 
 * @param {}
 *            activityId 按钮ID
 */
function doNew(activityId) {
	createDoc(activityId); // obpm.listView.js
}

function openWindowByType(action, title, viewType, actid) {
	var flag = false;
	
	if(actid){
		jQuery.ajax({
			type: 'POST',
			async:false, 
			url: contextPath + '/portal/dynaform/activity/runbeforeactionscript.action?_actid=' + actid,
			dataType : 'text',
			timeout: 3000,
			data: jQuery("#formList").serialize(),
			success:function(result) {
				if(result != null && result != ""){
		        	var jsmessage = eval("(" + result + ")");
		        	var type = jsmessage.type;
		        	var content = jsmessage.content;
		        	
		        	if(type == '16'){
		        		alert(content);
		        		document.forms[0].submit();
		        	}
		        	
		        	if(type == '32'){
		        		var rtn = window.confirm(content);
		        		if(!rtn){
		        			document.forms[0].submit();
		        		}else {
		        			flag = true;
		        		}
		        	}
		        }else {
		        	flag = true;
		        }
			},
			error: function(result) {
				alert("运行脚本出错");
			}
		});
	} else {
		flag = true;
	}

	if(flag){
		// View的openType(打开类型)
		var openType = OPEN_TYPE_NORMAL;
		var url = action;
	
		if (viewType == VIEW_TYPE_SUB) {
			var isRelate = document.getElementsByName("isRelate");
			url += "&isSubDoc=true";
			if (isRelate){
	            if (isRelate.length>0){
	            	var relate = isRelate[0].value;
	            	url += "&isRelate="+relate;
	            }
			}
		}
	
		if (document.getElementsByName("_openType")[0]) {
			if(typeof HAS_SUBFORM !="undefined" && HAS_SUBFORM){
				openType = document.getElementsByName("_openType")[0].innerText;
			}else{
				openType = document.getElementsByName("_openType")[0].value;
			}
		}
	
		var parameters = getQueryString();
	
		resetBackURL(); // view.js
		if (openType == OPEN_TYPE_NORMAL || openType == OPEN_TYPE_GRID) {
			if (isHomePage()) { // 首页单独处理
				url += "&_backURL="
						+ encodeURIComponent(contextPath
								+ "/portal/dispatch/homepage.jsp");
				url += "&" + parameters;
				parent.location = appendApplicationidByView(url);
			} else {
				document.forms[0].action = url;
				document.forms[0].submit();
			}
		} else if (openType == OPEN_TYPE_POP || openType == OPEN_TYPE_DIV) {
			var oSelects = document.getElementsByName("_selects");
			var _selects="";
			if(oSelects){
				for(var i=0;i<oSelects.length;i++){
					if(oSelects[i].checked){
						_selects+="&_selects="+oSelects[i].value;
					}
				}
			}
			url += "&" + parameters + "&openType=" + openType+_selects;
			url = appendApplicationidByView(url);
			var w = document.body.clientWidth - 25;
			showfrontframe({
						title : "",
						url : url,
						w : w,
						h : 30,
						windowObj : window.parent,
						callback : function(result) {
							setTimeout(function(){//通过右上角的关闭图标关闭弹出层后会显示加载进度条，加延迟后没有这个问题
								document.forms[0].submit();
							},1);
						}
					});
		} else if (openType == OPEN_TYPE_OWN) {
			var id = document.getElementsByName("_viewid")[0].value;
			if (viewType == VIEW_TYPE_SUB) {
				var sub_divid = parent.document.getElementById('sub_divid');
				var doc_obj = parent.document.getElementById(id);
				if (sub_divid) {
					sub_divid.value = doc_obj.src;
				}
	//			if (doc_obj) {
					//url += "&" + parameters;
					//doc_obj.src = url;
					document.forms[0].action = url;
					document.forms[0].submit();
	//			}
			} else {
				document.forms[0].action = url;
				document.forms[0].submit();
			}
	
		}
	}
}

/**
 * 检查URL是否带上application参数，没有就添加
 * <p>提交表单不能使用本方法：<code>document.forms[0].submit()</code></p>;
 * @param url 返回带有application参数的URL
 * @returns
 */
function appendApplicationidByView(url) {
	var appObject = document.getElementsByName("application")[0];
	if (appObject && url.indexOf("application") < 0) {
		if (url.indexOf("?") >= 0) {
			url += "&application=" + appObject.value;
		} else {
			url += "?application=" + appObject.value;
		}
	}
	return url;
}

/**
 * 父窗口是否为首页
 * 
 * @return {如果是返回true,否则false}
 */
function isHomePage() {
	if (parent
			&& parent.location.href.toLowerCase().substring(0,parent.location.href.toLowerCase().indexOf("?")).indexOf("homepage.jsp") != -1) {
		return true;
	}
	return false;
}

/**
 * 获取查询表单中的参数
 * 
 * @return {}
 */
function getQueryString(viewType) {
	var parameters = "";
	var isrelate = $(".control-content.active").find("[name='display_view']").attr("isrelate");
	if($(".control-content.active").length>0){
		parameters += "_viewid="
			+$(".control-content.active").find("span[name='_viewid']").text();
		if (isrelate != "false"){
			if (document.getElementsByName("parentid")[0]) {
				var parentid = "";
				$.each($("span[name='parentid']"),function(i,n){
					if($(n).text().length>0){
						parentid = $(n).text();
						return false;
					}
				});
				parameters += "&parentid="
						+ parentid;
			}
		}
	}else if (document.getElementsByName("_viewid")[0]) {
		parameters += "_viewid="
				+ document.getElementsByName("_viewid")[0].value;
		if (document.getElementsByName("parentid")[0]) {
			var parentid = "";
			$.each($("input[name='parentid']"),function(i,n){
				if($(n).val().length>0){
					parentid = $(n).val();
					return false;
				}
			});
			parameters += "&parentid="
					+ parentid;
		}
	}
	
	
	//子视图不拼接主表数据
	if (!viewType || viewType != "277") {
		if (typeof(dy_getValuesMap) == "function") {
			// dy_getValuesMap为SearchForm中的方法
			parameters += "&" + jQuery.param(dy_getValuesMap(false),true);
		}
	}
	return parameters;
}

/**
 * 文档删除 (DocumentDelete)
 * 
 * @param {}
 *            activityId 按钮ID
 */
function doRemove(activityId) {
	var checkboxs = document.getElementsByName("_selects");
	var isSelect = false;
	for (var i = 0; i < checkboxs.length; i++) {
		if (checkboxs[i].checked == true) {
			isSelect = true;
			break;
		}
	}
	if (isSelect) {
		if (activityId != null && activityId != "") {
			var rtn = window.confirm("确定要删除您选择的记录吗？");
			if (!rtn)
				return;
		}
	} else {
		alert("请选择要删除的数据！");
		return false;
	}

	ev_submit(activityId, false, "doDelete");
}

/**
 * 文档查询 (DocumentQuery)
 * 
 * @param {}
 *            activityId 按钮ID
 */
function doQuery(activityId) {
	ev_submit(activityId);
}

/**
 * 重新设置BackURL
 */
function resetBackURL() {
	var oBackURL = document.getElementsByName("_backURL")[0];
	if (oBackURL && oBackURL.value) {
		// 序列化查询表单字段
		var params = jQuery("form").serialize(); 
		//var backURL = oBackURL.value.substring(0, oBackURL.value.indexOf("?"));
		if (oBackURL.value.indexOf("?") != -1) {
			oBackURL.value = oBackURL.value;
			if (params) { // 没有查询表单
				var paramArrary = params.split("&");
				for(var i=0;i<paramArrary.length;i++){
					if(oBackURL.value.indexOf(paramArrary[i])!=-1 || paramArrary[i].indexOf("_backURL") != -1){//防止在backURL后面再拼多一次backURL,导致第二个backURL后面的参数不生效
					}else{
						oBackURL.value += "&" + paramArrary[i];
					}
				}
				
			}
		} else {
			oBackURL.value = oBackURL.value;
			if (params) {
				oBackURL.value += "?" + params;
			}
		}
	}
}

/**
 * 批量签章按钮对应的Function
 */
function DoBatchSignature() {
	if(navigator.userAgent.indexOf("MSIE")<0){
		alert("金格iSignature电子签章HTML版只支持IE，如果要签章请用IE浏览器");
		return;
	}
	var mLength = document.getElementsByName("_selects").length;
	var vItem;
	var DocumentList = "";
	for (var i = 0; i < mLength; i++) {
		vItem = document.getElementsByName("_selects")[i];
		if (vItem.checked) {
			if (i != mLength - 1) {
				DocumentList = DocumentList + vItem.value + ";";
			} else {
				DocumentList = DocumentList + vItem.value;
			}
		}
	}
	// alert(DocumentList);
	var ajax = null;

	if (window.ActiveXObject) {

		try {

			ajax = new ActiveXObject("Microsoft.XMLHTTP");

		} catch (e) {

			alert("创建Microsoft.XMLHTTP对象失败,AJAX不能正常运行.请检查您的浏览器设置.");
		}

	} else {

		if (window.XMLHttpRequest) {

			try {

				ajax = new XMLHttpRequest();

			} catch (e) {

				alert("创建XMLHttpRequest对象失败,AJAX不能正常运行.请检查您的浏览器设置.");
			}

		}
	}

	var url = document.getElementById("mGetBatchDocumentUrl").value;
	var mLoginname = document.getElementById("mLoginname").value;
	var DocumentID = document.getElementById("DocumentID").value;
	var ApplicationID = document.getElementById("ApplicationID").value
	var FormID = document.getElementById("FormID").value
	url = url + "?DocumentID=" + DocumentID + "&ApplicationID2="
			+ ApplicationID + "&FormID2=" + FormID;

	ajax.onreadystatechange = function() {

		if (ajax.readyState == 4 && ajax.status == 200) {

			if (ajax.responseText == "false") {

				return;
			}

			var documentName = ajax.responseText.split(',');
			// var buffer = [];
			var fildsList = "";
			for (var i = 0; i < documentName.length; i++) {

				if (i != documentName.length - 1) {
					// buffer.push(documentName[i]+"="+documentName[i]);
					fildsList = fildsList
							+ (documentName[i] + "=" + documentName[i] + ";");
				} else {
					// buffer.push(documentName[i]+"="+documentName[i]);
					fildsList = fildsList
							+ (documentName[i] + "=" + documentName[i]);
				}

			}
			// alert(fildsList);
			// buffer.join("");
			// alert(buffer);
			// alert(formList.SignatureControl);
			if (formList.SignatureControl != null) {
				if (DocumentList == "") {
					alert("请选择需要签章的文档。");
				}
				formList.SignatureControl.FieldsList = fildsList; // 所保护字段
				formList.SignatureControl.Position(460, 275); // 签章位置
				formList.SignatureControl.DocumentList = DocumentList; // 签章页面ID
				formList.SignatureControl.WebSetFontOther("True", "同意通过", "0",
						"宋体", "11", "000128", "True"); // 默认签章附加信息及字体,具体参数信息参阅技术白皮书
				formList.SignatureControl.SaveHistory = "false"; // 是否自动保存历史记录,true保存
				// false不保存
				// 默认值false
				formList.SignatureControl.UserName = "lyj"; // 文件版签章用户
				formList.SignatureControl.WebCancelOrder = 0; // 签章撤消原则设置,
				// 0无顺序 1先进后出
				// 2先进先出 默认值0
				// formList.SignatureControl.DivId = "contentTable"; //签章所在层
				formList.SignatureControl.AutoCloseBatchWindow = true;
				formList.SignatureControl.RunBatchSignature();
			} else {
				alert("请安装金格iSignature电子签章HTML版软件");
				document.getElementById("hreftest2").click();
			}

		}

	};

	ajax.open("POST", url);

	ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	ajax.send(null);

}

/**
 * 刷新父窗口
 */
function ev_reloadParent() {
	try {
		// operation属性在detail.jsp页面中获取
		if (operation != 'doSave' && operation != 'doDelete') {
			return;
		}

		if (parent) {
			if (parent.treeview) { // 刷新父窗口树型对象
				parent.selectedNode = OBPM("#treedocid").val();
//				parent.treeview.jstree("refresh", "#"
//								+ OBPM("#treedocid").val());
				parent.treeview.jstree("refresh", "#root");//替代了上面的方法，刷新整棵树，临时处理，会影响性能
			}
		}
	} catch (ex) {
	}
}

/** *********front js********* */
function showAction(viewMode, addType) {
	document.getElementsByName("viewMode")[0].value = viewMode;

	// var url = '<%=request.getAttribute("backURL")%>';
	//var url = document.getElementById("backURL").value;
	var url = formList.action;
	var index = url.indexOf('?');
	if(index > -1){
		url = url.substring(0, index);
	}
	if (addType) {
		url += '?addType=' + addType;
		//url += '&application=' + document.getElementsByName("_application")[0].value;
	}else{
		//url += '?application=' + document.getElementsByName("_application")[0].value;
	}
	formList.action = url;
	formList.submit();
}

function showActions(viewMode) {
	showAction(viewMode, null);
}

function ShowDayView(currDate) {
	document.forms[0].action = 'displayView.action?_viewid='
			+ document.getElementById("displayView__viewid").value + '&currentDate='
			+ currDate + '&isinner=true';
	document.formList.submit();
}

// valueMap是一个对象。
function ev_selectone(valueMap) {
	var value = jQuery.json2Str(valueMap);
	if (isEdit != false && isEdit != 'false') {
		OBPM.dialog.doReturn("{id:" + value + "}");
	}
}

// 隐藏iframe
function hidden() {
	var id = document.getElementById("_viewid").value;
	var doc_obj = parent.document.getElementById(id);
	// alert(doc_obj);
	if (doc_obj) {
		doc_obj.style.display = 'none';
	}
}

// 调整窗口
function adjustDataIteratorSize() {
	var container = document.getElementById("container");
	var searchForm = document.getElementById("searchFormTable");
	var pageTable = document.getElementById("pageTable");
	var activityTable = document.getElementById("activityTable");
	var dataTable = document.getElementById("dataTable");
	var containerHeight = document.body.clientHeight;
	if (containerHeight > 0) {
		container.style.height = containerHeight + 'px';
		var dataTableHeight = containerHeight;
	}
	
	if (dataTableHeight > 0) {
		if (activityTable) {
			dataTableHeight = dataTableHeight - activityTable.offsetHeight;
		}
		if (searchForm) {
			dataTableHeight = dataTableHeight - searchForm.offsetHeight;
		}
		if (pageTable) {
			dataTableHeight = dataTableHeight - pageTable.offsetHeight;
		}
		dataTable.style.width = "100%";
		if(dataTableHeight > 7)
			dataTable.style.height = dataTableHeight - 7 + 'px';
	}
	
	container.style.visibility = "visible";
}

/**
 * 生成帮助 BuildHelp
 * 
 * @param {}
 *            activityId 按钮ID
 */
function doBuild(activityId) {
	ev_submit(activityId, false, "");
}

/*
 * 视图打印
 */

function ev_printview(actid) {
	var viewid = document.getElementsByName("_viewid")[0].value;
	var signatureExist = document.getElementById("signatureExist").value;
	var url = activityAction;
	url += '?_signatureExist=' + signatureExist;
	url += '&_activityid=' + actid;  
	url += '&isprint=true';
	
	if(ev_runbeforeScriptforview(actid)){
		var fmmy = document.forms[0]; 
		fmmy.action=url;
		fmwin = window.open("about:blank", "_my_submit_win");   
		fmmy.target="_my_submit_win"; 
		fmmy.submit();
		fmmy.target="";
	}
	
//	var viewid = document.getElementsByName("_viewid")[0].value;
//	var signatureExist = document.getElementById("signatureExist").value;
//	
//	var url = activityAction;
//	url += '?_signatureExist=' + signatureExist;
//	url += '&_activityid=' + actid;  
//	url += '&isprint=true';
//	 var fmmy = document.forms[0]; 
//	 fmmy.action=url;
//	 fmwin = window.open("about:blank", "_my_submit_win");   
//	 fmmy.target="_my_submit_win"; 
//	 fmmy.submit();
	 
}
/*
 * 显示右边任务内容
 */
function showTaskContent(index){
}

//JumpTo操作按钮 点击事件
function ev_JumpTo(activityId, jumpType, targetList, jumpMode) {
	ev_dispatcherPage(activityId);
}

/**
 * Dispatcher按钮
 * @param actid
 * @return
 */
function ev_dispatcherPage(actid){
	if(ev_runbeforeScriptforview(actid)){
		var url = activityAction + '?_activityid=' + actid;
    	document.forms[0].action = url;
    	//document.forms[0].target = '_blank';
    	document.forms[0].submit();
    	//document.forms[0].target = '';
	}
}

function modifyActionBack(){
	document.getElementsByName('_currpage')[0].value=1;
	var viewid = jQuery("input[name='_viewid']").val();
	document.forms[0].action=contextPath + "/portal/dynaform/view/displayView.action?isQueryButton=true&_viewid=" + viewid;
	jQuery("input[name='_viewid']").remove();
	document.forms[0].target="";
	document.forms[0].submit();
    }

function ev_subFormView(){
	document.getElementsByName('_currpage')[0].value=1;
	document.forms[0].action=contextPath + "/portal/dynaform/view/subFormView.action?isQueryButton=true";
	document.forms[0].target="";
	document.forms[0].submit();
}

function runscript(docid, colid,obj){
	var data = {};
	var url = contextPath + '/portal/dynaform/view/runScript.action' + '?docid=' + docid + '&columnId=' + colid;
	if(obj){
		var view = jQuery(obj).parents("div[type=includedView]");
		var data = DisplayView.span2Json(obj);
	}else{
		data = jQuery(document.forms[0]).serialize();
	}
	
	jQuery.post(url,data,function(result){
		if(result && result.length>0){
			alert(result);
		}
		if(obj){
			var action = DisplayView.getAction(obj);
			DisplayView.postForm(obj,action,json);
		}else{
			document.forms[0].submit();
		}
		
	});
}

//操作列跳转按钮的方法
function jumptoform(formId, jumpMapping, title){
	var showW = top.document.documentElement.clientWidth;
	showW = showW - showW/10;
	var showH = top.document.documentElement.clientHeight;
	showH = showH - showH/10;
	var obj = eval(jumpMapping);
	var str = "";
	if(obj){
		str += "&";
		for(var i=0; i<obj.length; i++){
			var agr = obj[i].name;
			var colValue = obj[i].value;
			str += encodeURIComponent(agr) + "=" + encodeURIComponent(colValue) + "&";
		}
		str = str.substring(0, str.length-1);
	}

	var applicationid = document.getElementById("ApplicationID").value;
	var view_id = document.getElementsByName("_viewid")[0].value;
	var newAction = contextPath + '/portal/dynaform/document/newWithJump.action';
	var url = newAction + "?applicationid=" + applicationid + "&application=" + applicationid 
				+ "&_formid=" + formId + "&view_id=" + view_id + "&_isJump=1" + str
				+ "&_backURL=isJumpToForm";
	
	showfrontframe({
		title : title,
		url : url,
		w : showW,
		h : showH,
		windowObj : window.parent,
		callback : function(result) {
		}
	});
}
; 
/**
 * 	后台预览的时候判断页面是否重构完成
 */
var isComplete = false; 

/**
 * 子视图公用初始化方法
 * @return
 */
function initDispComm(){
	openDownloadWindow(openDownWinStr);	// 打开Excel下载窗口
	setTimeout(function(){
		jqRefactor4DisplayView(); //DisplayView重构
		//jQuery("div[moduleType='viewFileManager']").obpmViewFileManager();  	//内嵌视图文件管理功能
		//jQuery("div[moduleType='viewTakePhoto']").obpmViewTakePhoto();  	//内嵌视图在线拍照功能
		//jQuery("div[moduleType='viewImageUpload']").obpmViewImageUpload();  	//内嵌视图图片上传功能
		//jQuery("div[moduleType='viewImageUpload2DataBase']").obpmViewImageUpload2DataBase();  	//内嵌视图图片上传到数据库功能
	},50);
	setTimeout(function(){
		//jqRefactor();//表单控件jquery重构
	},10);
	selectData4Doc(); //回选列表数据
	setTimeout(function(){
		showPromptMsg();	//显示提示信息
	},300);
	document.ondblclick=handleDbClick;
	setTimeout(function(){
		dataTableSize();
		//ev_resize4IncludeViewPar();	//包含元素包含视图时调整iframe高度
	},300);
	ev_reloadParent();	//刷新父窗口
	isComplete = true; //后台预览的时候判断页面是否重构完成
}

//给后台preview.jsp视图预览的时候判断页面是否重构完成
function getIsComplete(){
	return isComplete ;
}

function zoompage() {
 	var typeflage = typeof(dialogArguments);
 	if(typeflage != 'undefined' ) {    
    	
    }else if(window.opener){
    	
    }else{
        window.top.IsResize();
    }
 	ev_resize4IncludeViewPar();	//包含元素包含视图时调整iframe高度
}
//设置数据容器的高度。
function dataTableSize(){
	//包含元素固定高度时不重新布局
	var divid = document.getElementsByName("divid")[0].value;
	var pSpan = parent.document.getElementById(divid);
	var pIframe = "";
	var fixation = "false";
	if(pSpan){
		pIframe = pSpan.getElementsByTagName("iframe")[0];
		if(pIframe){
			fixation = pIframe.getAttribute("fixation");
		}
	}
	if(fixation=="true") return;
	
	var dataTableH = jQuery("#dataTable").height();
	if(dataTableH < 100){
		dataTableH = 120;
	}else if(dataTableH >300){
		dataTableH = 300;
	}else{
		dataTableH = dataTableH + 20;
	}
	jQuery("#table_container").height(dataTableH);
}
function handleDbClick(){
	if(event.srcElement.onclick){
	}else if(event.srcElement.type!=null&&(event.srcElement.type.toUpperCase()=='SUBMIT'
		||event.srcElement.type.toUpperCase()=='BUTTON'
		||event.srcElement.type.toUpperCase()=='CHECKBOX'
		||event.srcElement.type.toUpperCase()=='RADIO'
		||event.srcElement.type.toUpperCase()=='SELECT'
		||event.srcElement.type.toUpperCase()=='IMG')){
	  
	}else{
		zoompage();
	}
}

/**
 * 显示提示信息
 * for:default/gentle/fresh/dwz/brisk/blue
 */
function showPromptMsg(){
	var funName = typeName;
	var url = urlValue;
	url+="&_refreshDocument=true";
	var msg = document.getElementsByName("message")[0].value;
	if (msg) {
		try{
			eval("do" + funName + "(msg , url);");
		} catch(ex) {
		}
	}
}

function on_unload() {
	ev_reloadParent();	//刷新父窗口
}

function createDoc(activityid) {
	// 查看/script/view.js
	var action = activityAction + "?_activityid=" + activityid;
	openWindowByType(action,newStr, VIEW_TYPE_SUB, activityid); 
}

function viewDoc(docid, formid ,signatureExist,templateForm,isEdit,instanceId,nodeId) {
	// 查看/script/view.js
	var url = docviewAction;
	url += '?_docid=' + docid;
	if (formid) {
		url += '&_formid=' +  formid;
	}
	if (templateForm) {
		url += '&_templateForm=' +  templateForm;
	}
	if(signatureExist){
		url += '&signatureExist=' +  signatureExist;
	}
	if(isedit){
		url += '&isedit=' +  isedit+"";
		//url += '&show_act=' +  isedit+"";
		
	}
	if(instanceId){
		url += '&_targetInstance=' +  instanceId;
	}
	if(nodeId){
		url += '&_targetNode=' +  nodeId;
	}
	openWindowByType(url,selectStr, VIEW_TYPE_SUB); 
}

/**
 * 包含元素包含视图时调整iframe高度
 * from:blue/brisk/default/dwz/fresh/gentle
 */
function ev_resize4IncludeViewPar(){
	var divid = document.getElementsByName("divid")[0].value;
	var _viewid = document.getElementsByName("_viewid")[0].value;
	ev_resize4IncludeView(divid,_viewid,'DISPLAYVIEW');
}

/**
 * 重构displayView
 */
(function($){
	$.fn.obpmDisplayView = function(){
		return this.each(function(){
			
			var $dvid = $(this).parents(".control-content").attr("id");
			$(this).next(".ui-content").attr("id","view_"+$dvid);
			
			var Column = {
					COLUMN_TYPE_SCRIPT : 'COLUMN_TYPE_SCRIPT',	//脚本编辑模式
					COLUMN_TYPE_FIELD : 'COLUMN_TYPE_FIELD',	//视图编辑模式
					COLUMN_TYPE_OPERATE : 'COLUMN_TYPE_OPERATE',//操作列
					COLUMN_TYPE_LOGO : 'COLUMN_TYPE_LOGO',		//图标列
					COLUMN_TYPE_ROWNUM : 'COLUMN_TYPE_ROWNUM',	//序号列
					DISPLAY_ALL : '00',//列显示全部文本
					DISPLAY_PART : '01'//列显示部分文本
			},
			ColumnOperaType = {
					BUTTON_TYPE_DELETE : "00",
					BUTTON_TYPE_DOFLOW : "01",
					BUTTON_TYPE_TEMPFORM : "03",
					BUTTON_TYPE_SCRIPT : "04",
					BUTTON_TYPE_JUMP : "05"//操作列增加跳转类型按钮
			},
			View = {
					DISPLAY_TYPE_TEMPLATEFORM : "templateForm"
			},
			Setting = {//
					TABLE_CLASS : 'listDataTable',		//表格class
					TH_CLASS : 'listDataTh',						//标题行class
					TH_FIRST_TD_CLASS : 'listDataThFirstTd',			//标题行第一个单元格class
					TH_TD_CLASS : 'listDataThTd',		//标题行其他单元格class
					TR_FIRST_TD_CLASS : 'listDataTrFirstTd',		//数据行第一个单元格class
					TR_TD_CLASS : 'listDataTrTd',		//数据行其他单元格class
					TR_CLASS : 'listDataTr',				//数据行class
					TR_OVER_CLASS : 'listDataTr_over'	//数据行滑过class
			},
			
			/**
			 * 重构数据行td
			 */
			toDataTdHtml = function($tdField){
					var tdAttrs = {};
					tdAttrs.displayType = $tdField.attr('displayType');
					tdAttrs.isHidden = $tdField.attr('isHidden');
					tdAttrs.colWidth = $tdField.attr('colWidth');
					tdAttrs.colGroundColor = $tdField.attr('colGroundColor');
					tdAttrs.colColor = $tdField.attr('colColor');
					tdAttrs.colFontSize = $tdField.attr('colFontSize');
					tdAttrs.isVisible = $tdField.attr('isVisible');
					tdAttrs.isReadonly = $tdField.attr('isReadonly');
					tdAttrs.colType = $tdField.attr('colType');
					tdAttrs.fieldInstanceOfWordField = $tdField.attr('fieldInstanceOfWordField');
					tdAttrs.fieldInstanceOfMapField = $tdField.attr('fieldInstanceOfMapField');
					tdAttrs.displayType = $tdField.attr('displayType');
					tdAttrs.isShowTitle = $tdField.attr('isShowTitle');
					tdAttrs.colDisplayLength = $tdField.attr('colDisplayLength');
					tdAttrs.colFieldName = $tdField.attr('colFieldName');
					tdAttrs.colFlowReturnCss = $tdField.attr('colFlowReturnCss');
					tdAttrs.viewDisplayType = $tdField.attr('viewDisplayType');

					tdAttrs.isSignatureExist = $tdField.attr('isSignatureExist');
					tdAttrs.isEdit = $tdField.attr('isEdit');
					tdAttrs.colButtonType = $tdField.attr('colButtonType');
					tdAttrs.colApproveLimit = $tdField.attr('colApproveLimit');
					tdAttrs.colButtonName = $tdField.attr('colButtonName');
					tdAttrs.colMappingform = $tdField.attr('colMappingform');
					
					tdAttrs.result = $tdField.find("div[name='result']").html();
					tdAttrs.colIcon = $tdField.attr('colIcon');
					tdAttrs.colId = $tdField.attr("colId");
					tdAttrs.colTemplateForm = $tdField.attr("colTemplateForm");
					tdAttrs.showword = $tdField.attr("showword");
				
					var tdHtml = '';
					var pHtml = '';
					var aHtml = '';
				
					//tdAttrs.displayType = (tdAttrs.displayType == 'true'?true:false);
					tdAttrs.isShowTitle = (tdAttrs.isShowTitle == 'true'?true:false);
					tdAttrs.result = (tdAttrs.result?tdAttrs.result:'');
					
					tdAttrs.colWidth = (tdAttrs.colWidth != "null")?tdAttrs.colWidth:'';
					tdAttrs.colGroundColor = (tdAttrs.colGroundColor && tdAttrs.colGroundColor != "null" && tdAttrs.colGroundColor != "FFFFFF")?tdAttrs.colGroundColor:'';
					tdAttrs.colColor = (tdAttrs.colColor && tdAttrs.colColor != "null" && tdAttrs.colColor != "000000")?tdAttrs.colColor:'';
					tdAttrs.colFontSize = (tdAttrs.colFontSize && tdAttrs.colFontSize != "null" && tdAttrs.colFontSize != "12")?tdAttrs.colFontSize:'';
					tdAttrs.isVisible = (tdAttrs.isVisible == 'true'?true:false);
					tdAttrs.isReadonly = (tdAttrs.isReadonly == 'true'?true:false);
					tdAttrs.colType = tdAttrs.colType?tdAttrs.colType:"";
					tdAttrs.fieldInstanceOfWordField = (tdAttrs.fieldInstanceOfWordField == 'true'?true:false);
					tdAttrs.fieldInstanceOfMapField = (tdAttrs.fieldInstanceOfMapField == 'true'?true:false);
					//tdAttrs.displayType = tdAttrs.displayType?tdAttrs.displayType:"";
					tdAttrs.isShowTitle = (tdAttrs.isShowTitle == 'true'?true:false);

					tdAttrs.colDisplayLength = tdAttrs.colDisplayLength?tdAttrs.colDisplayLength:"";
					tdAttrs.colFieldName = tdAttrs.colFieldName?tdAttrs.colFieldName:"";
					tdAttrs.colFlowReturnCss = (tdAttrs.colFlowReturnCss == 'true'?true:false);
					tdAttrs.viewDisplayType = (tdAttrs.viewDisplayType != "null")?tdAttrs.viewDisplayType:'';
					
					tdAttrs.isSignatureExist = (tdAttrs.isSignatureExist == 'true'?true:false);
					tdAttrs.isEdit = (tdAttrs.isEdit == 'true'?true:false);
					tdAttrs.isHidden = (tdAttrs.isHidden == 'true'?true:false);
					tdAttrs.colButtonType = (tdAttrs.colButtonType != "null")?tdAttrs.colButtonType:'';
					tdAttrs.colApproveLimit = (tdAttrs.colApproveLimit != "null")?tdAttrs.colApproveLimit:'';
					tdAttrs.colButtonName = (tdAttrs.colButtonName != "null")?tdAttrs.colButtonName:'';
					tdAttrs.colMappingform = (tdAttrs.colMappingform != "null")?tdAttrs.colMappingform:'';
					tdAttrs.colIcon = (tdAttrs.colIcon != "null")?tdAttrs.colIcon:'';
					tdAttrs.showIcon =  ($tdField.attr('showIcon') != null) ? $tdField.attr('showIcon'):'';
					
					if(tdAttrs.result.indexOf("[")==0){
						tdAttrs.title = $tdField.find("div[name='result']").text();
					}

				var docId = $tdField.attr('docId');
				docId = docId?docId:'';
				
				//var title = $tdField.attr('title');
				var tip = "";
				//if(title.indexOf("<table>") == -1)
				//	tip = title;
				
				var viewTemplateForm = $tdField.attr('viewTemplateForm');
				viewTemplateForm = (viewTemplateForm != "null")?viewTemplateForm:'';
									
				var docFormid = $tdField.attr('docFormid');
				docFormid = docFormid?docFormid:'';
				
				var jumpMapping = $tdField.find("div[name='jumpMapping']").html();
				jumpMapping = jumpMapping?jumpMapping:'';
				
				var result = $tdField.find("div[name='result']").html();
				result = (result?result:'');
				
				if(tdAttrs.showIcon){
					result = "<img style='' src='../../../lib/icon/" + tdAttrs.showIcon+ "'/>";
				}

				var convert2HTMLEncode = function(str){
					var s = str;
					if(Column.COLUMN_TYPE_FIELD == tdAttrs.colType && !tdAttrs.colFieldName.substr(0,1) == "$" && !tdAttrs.colFlowReturnCss){
						s = s.replace(new RegExp(">","gm"),"&gt;");
						s = s.replace(new RegExp("<","gm"),"&lt;");
					}
					return s;
				};
				//多流程状态时数据处理
				var result2tdHtml = function(){
					var templateForm = "";
					if(View.DISPLAY_TYPE_TEMPLATEFORM == tdAttrs.viewDisplayType){
						templateForm = viewTemplateForm;
					}
					var resHtml = "";
					if("$StateLabel" == tdAttrs.colFieldName && (result.indexOf("[")==0 || result.indexOf("<img")==0)){//视图列绑定流程状态字段类型
						//解析json数据生成html
						resHtml += "<TABLE style=\"width:100%;border:0;\">";
						var instances;
						if(result.indexOf("[")==0){
							instances = JSON.parse(result);
						}else if(result.indexOf("<img")==0){
							var jsonStartIndex = result.indexOf("[{"),
								jsonEndIndex = result.lastIndexOf("}]"),
								imgHtml = result.substring(0,result.indexOf("<font")),
								fontStart = result.substring(result.indexOf("<font"),jsonStartIndex),
								fontEnd = result.substring(jsonEndIndex + 2,result.length);
							instances = result.substring(jsonStartIndex,jsonEndIndex + 2);
							instances = eval("(" + instances + ")");
						}
						for(var i=0;i<instances.length;i++){
							if(i+1==instances.length){
								resHtml += "<tr><td style=\"line-height:16px;border-right-width:0;border-bottom-width:0; border-right-style: none;\">";
							}else{
								resHtml += "<tr><td style=\"line-height:16px;border-right-width: 0px; border-right-style: none;\">";
							}
							var instance = instances[i];
							var instanceId = instance.instanceId;
							
							var nodes = instance.nodes;
							if(result.indexOf("<img")==0){
								resHtml += imgHtml;
							}
							for(var j=0;j<nodes.length;j++){
								var node = nodes[j];
								var nodeId = node.nodeId;
								var stateLable = truncationText(node.stateLabel,tdAttrs.displayType,tdAttrs.colDisplayLength);
								//只读
								if(tdAttrs.isReadonly){
									resHtml += stateLable;
								}else {
									resHtml += "<a href=\"javaScript:viewDoc('";
									resHtml += docId + "', '";
									resHtml += docFormid + "', '";
									resHtml += tdAttrs.isSignatureExist + "', '";
									resHtml += templateForm + "', '";
									resHtml += tdAttrs.isEdit + "', '";
									resHtml += instanceId + "', '";
									resHtml += nodeId + "')\">";
									if(result.indexOf("<img")==0){
										stateLable = fontStart + stateLable + fontEnd;
									}
									resHtml += stateLable+"</a>&nbsp;&nbsp;";
								}
							}
							resHtml += "</td></tr>";
						}
						resHtml += "</TABLE>";
					
					}else if("$PrevAuditNode" == tdAttrs.colFieldName && result.indexOf("[")==0){//视图列绑定上一环节流程处理节点名称字段
						//解析json数据生成html
						resHtml += "<TABLE style=\"width:100%;border:0;\">";
						var instances = JSON.parse(result);
						for(var i=0;i<instances.length;i++){
							var instance = instances[i];
							var instanceId = instance.instanceId;
							var prevAuditNode = instance.prevAuditNode;

							if(i+1==instances.length){
								resHtml += "<tr><td title=\""+prevAuditNode+"\" style=\"line-height:16px;border-right-width:0;border-bottom-width:0; border-right-style: none;\">";
							}else{
								resHtml += "<tr><td title=\""+prevAuditNode+"\" style=\"line-height:16px;border-right-width: 0px; border-right-style: none;\">";
							}
							//只读
							if(tdAttrs.isReadonly){
								resHtml += truncationText(prevAuditNode,tdAttrs.displayType,tdAttrs.colDisplayLength);
							}else {
								resHtml += "<a href=\"javaScript:viewDoc('";
								resHtml += docId + "', '";
								resHtml += docFormid + "', '";
								resHtml += tdAttrs.isSignatureExist + "', '";
								resHtml += templateForm + "', '";
								resHtml += tdAttrs.isEdit + "', '";
								resHtml += instanceId + "')\">";
								resHtml += truncationText(prevAuditNode,tdAttrs.displayType,tdAttrs.colDisplayLength)+"</a>&nbsp;&nbsp;";
							}
							resHtml += "</td></tr>";
						}
						resHtml += "</TABLE>";
					
					}else if("$PrevAuditUser" == tdAttrs.colFieldName && result.indexOf("[")==0){//视图列绑定上一环节流程处理节点名称字段
						//解析json数据生成html
						resHtml += "<TABLE style=\"width:100%;border:0;\">";
						var instances = JSON.parse(result);
						for(var i=0;i<instances.length;i++){
							var instance = instances[i];
							var instanceId = instance.instanceId;
							var prevAuditUser = instance.prevAuditUser;

							if(i+1==instances.length){
								resHtml += "<tr><td title=\""+prevAuditUser+"\" style=\"line-height:16px;border-right-width:0;border-bottom-width:0; border-right-style: none;\">";
							}else{
								resHtml += "<tr><td title=\""+prevAuditUser+"\" style=\"line-height:16px;border-right-width: 0px; border-right-style: none;\">";
							}
							//只读
							if(tdAttrs.isReadonly){
								resHtml += truncationText(prevAuditUser,tdAttrs.displayType,tdAttrs.colDisplayLength);
							}else {
								resHtml += "<a href=\"javaScript:viewDoc('";
								resHtml += docId + "', '";
								resHtml += docFormid + "', '";
								resHtml += tdAttrs.isSignatureExist + "', '";
								resHtml += templateForm + "', '";
								resHtml += tdAttrs.isEdit + "', '";
								resHtml += instanceId + "')\">";
								resHtml += truncationText(prevAuditUser,tdAttrs.displayType,tdAttrs.colDisplayLength)+"</a>&nbsp;&nbsp;";
							}
							resHtml += "</td></tr>";
						}
						resHtml += "</TABLE>";
					
					}else {
						resHtml = truncationText(result,tdAttrs.displayType,tdAttrs.colDisplayLength);
					}
					return resHtml;
				};
				if(!tdAttrs.isHidden){
					// 宽度为0时隐藏
					if((tdAttrs.colWidth && tdAttrs.colWidth == '0') || !tdAttrs.isVisible || tdAttrs.isHidden ){
						tdHtml += "<td class='" + Setting.TR_TD_CLASS + "' style='display: none;'";
					}else if(tdAttrs.colGroundColor != ""){//如果设置了底色,加上底色
						tdHtml += "<td class='" + Setting.TR_TD_CLASS + "' style='background-color:#" + tdAttrs.colGroundColor + ";word-break: break-all;'";
					}else{
						tdHtml += "<td class='" + Setting.TR_TD_CLASS + "' style='word-break: break-all;'";
					}
					
					if(!(tdAttrs.isReadonly || tdAttrs.colType == "COLUMN_TYPE_LOGO") && result.indexOf("<TABLE>") == -1){
						tdHtml += " onClick=\"viewDoc ('"
							+ docId + "', '"
							+ docFormid + "', '"
							+ tdAttrs.isSignatureExist + "', '"
							+ templateForm + "', '"
							+ tdAttrs.isEdit + "')\"";
					}
					
					tdHtml += ">";
					
					//只读或logo列或列字段或word控件字段
					if(tdAttrs.isReadonly || tdAttrs.colType == "COLUMN_TYPE_LOGO" || tdAttrs.fieldInstanceOfWordField || tdAttrs.fieldInstanceOfMapField){//|| !tdAttrs.isEdit  ) {
						if(!tdAttrs.fieldInstanceOfWordField && !tdAttrs.fieldInstanceOfMapField){
							var pHtml = "";
							pHtml += "<p";
							if(tdAttrs.isShowTitle)
								pHtml += " title='" + title + "'";

							//如果有设置字体大小及颜色
							if((tdAttrs.colColor != "") || (tdAttrs.colFontSize != "")){
								pHtml += " style='";
								if(tdAttrs.colColor != ""){
									pHtml += "color:#" + tdAttrs.colColor + ";";
								}
								if(tdAttrs.colFontSize != ""){
									pHtml += "font-size:" + tdAttrs.colFontSize + "px;";
								}
								pHtml += "'";
							}
							pHtml += " >";
							
							//子流程标签和处理人数据处理
							if(tdAttrs.isReadonly){
								if(result != null){
									result = result2tdHtml();
								}
							}
							
							
							if(Column.DISPLAY_ALL == tdAttrs.displayType){
								pHtml += convert2HTMLEncode(result) + "</p>";
							}else{
								pHtml += convert2HTMLEncode(result) + "</p>";
							}
							tdHtml += pHtml;
						}
					}else{
						if(result != null){
							var aHtml = "";
							if(result.toLowerCase().indexOf("<a ") != -1 
									|| result.toLowerCase().indexOf("<a>") != -1
									|| (result.toLowerCase().indexOf("<input ") != -1 
									&& (result.toLowerCase().indexOf("type='button'") != -1 
									|| result.toLowerCase().indexOf("type=button") != -1))
									|| result.toLowerCase().indexOf("viewdoc") != -1){
								aHtml += result;
							}else{
								//子流程标签和处理人数据处理
								result = result2tdHtml();
								
								var templateForm = "";
								if(View.DISPLAY_TYPE_TEMPLATEFORM == tdAttrs.viewDisplayType){
									templateForm = viewTemplateForm;
								}
								if(result.indexOf("<TABLE>") != -1  || !tdAttrs.isEdit){
//									aHtml += "<div style=\"cursor:pointer;\" onclick=\"javaScript:viewDoc('";
									aHtml += "<div style=\"cursor:pointer;\" ";
								}else{
									aHtml += "<a ";
//									aHtml += "<a href=\"javaScript:viewDoc('";
//									aHtml += docId + "', '";
//									aHtml += docFormid + "', '";
//									aHtml += tdAttrs.isSignatureExist + "', '";
//									aHtml += templateForm + "', '";
//									aHtml += tdAttrs.isEdit + "')\"";
								}
								
								//如果有设置字体大小及颜色
								if((tdAttrs.colColor != "") || (tdAttrs.colFontSize != "")){
									aHtml += " style='";
									if(tdAttrs.colColor != ""){
										aHtml += "color:#" + tdAttrs.colColor + ";";
									}
									if(tdAttrs.colFontSize != ""){
										aHtml += "font-size:" + tdAttrs.colFontSize + "px;";
									}
									aHtml += "'";
								}
									
								if(result.indexOf("img") != -1) {
									if(tdAttrs.isShowTitle)
										aHtml += " title='" + tdAttrs.title + "'";
										
									aHtml += " >";
									if(result.indexOf("<TABLE>") != -1){
										aHtml += convert2HTMLEncode(result) + "</div>";
									}else{
										aHtml += convert2HTMLEncode(result) + "</a>";
									}
								}else{
									if(tdAttrs.isShowTitle)
										aHtml += " title='" + tdAttrs.title + "'";
									aHtml += " >";
									if(result.indexOf("<TABLE>") != -1){
										aHtml += convert2HTMLEncode(result) + "</div>";
									}else{
										aHtml += convert2HTMLEncode(result) + "</a>";
									}
								}
							}
						}
						tdHtml += aHtml;
					}
					
					//操作列
					if("COLUMN_TYPE_OPERATE" == tdAttrs.colType && ColumnOperaType.BUTTON_TYPE_DELETE == tdAttrs.colButtonType){
						var inputHtml = "<input type=button value='" + tdAttrs.colButtonName;
						
						inputHtml += "' onclick=\"on_delete('"+docId+"')\" ";
						inputHtml += "/>";
						
						tdHtml += inputHtml;
						
					}else if("COLUMN_TYPE_OPERATE" == tdAttrs.colType && ColumnOperaType.BUTTON_TYPE_DOFLOW == tdAttrs.colButtonType){
						var inputHtml = "<input type=button value='" + tdAttrs.colButtonName;

						inputHtml += "' onclick=\"on_doflow('"+docId+"','"+tdAttrs.colApproveLimit+"')\" ";
						inputHtml += "/>";
						
						tdHtml += inputHtml;
						
					}else if("COLUMN_TYPE_OPERATE" == tdAttrs.colType && ColumnOperaType.BUTTON_TYPE_TEMPFORM == tdAttrs.colButtonType){
						var inputHtml = "<input type=button value='" + tdAttrs.colButtonName;
						
						inputHtml += "' onclick=\"viewDoc('"+docId+"','"+docFormid+"','"+tdAttrs.isSignatureExist+"','"+tdAttrs.colTemplateForm+"')\" ";
						inputHtml += "/>";
						
						tdHtml += inputHtml;
						
					}else if("COLUMN_TYPE_OPERATE" == tdAttrs.colType && ColumnOperaType.BUTTON_TYPE_SCRIPT == tdAttrs.colButtonType){
						var inputHtml = "<input type=button value='" + tdAttrs.colButtonName;

						inputHtml += "' onclick=\"runscript('"+docId+"','"+tdAttrs.colId+"')\" ";
						inputHtml += "/>";
						
						tdHtml += inputHtml;
						
					}else if("COLUMN_TYPE_OPERATE" == tdAttrs.colType && ColumnOperaType.BUTTON_TYPE_JUMP == tdAttrs.colButtonType){
						var inputHtml = "<input type=button value='" + tdAttrs.colButtonName;

						inputHtml += "' onclick=\"jumptoform('"+tdAttrs.colMappingform+"',"+jumpMapping+",'"+tdAttrs.colButtonName+"')\" ";
						inputHtml += "/>";
						
						tdHtml += inputHtml;						
					}
					//logo列
					if("COLUMN_TYPE_LOGO" == tdAttrs.colType && tdAttrs.colIcon && tdAttrs.colIcon != ""){
						tdHtml += "<img style='' src='../../../lib/icon/" + tdAttrs.colIcon+ "'/>";
					}
					
					if(tdAttrs.fieldInstanceOfWordField){
						var btnHtml = "<img src='../../share/images/view/word.gif'";
						
						btnHtml += " onclick=\"showWordDialogWithView('"+tdAttrs.showword+"','WordControl','"+docId+"','"+result+"','"+tdAttrs.colFieldName+"',3,2,false,true)\" ></img>";
						
						tdHtml += btnHtml;					
					}else if(tdAttrs.fieldInstanceOfMapField){
						var application = jQuery("body",parent.document).find("#application").val();
						var fieldVal = "";
						var displayType = 1;
						var f_id = docId + "_" + tdAttrs.colFieldName;
						var valhtml = tdAttrs.title == " "?"":"value = '" + tdAttrs.title + "'";
						var btnHtml = "<input type='hidden' id = '" + f_id + "' " + valhtml + ">";
						btnHtml += "<img src='../../share/images/view/map.png' style='margin: 0 5px;'";
						btnHtml += " onclick=\"FormBaiduMap('";
						btnHtml += f_id + "', '";
						btnHtml += application + "', '";
						btnHtml += displayType + "')\"";
						
						tdHtml += btnHtml;
					}else if (result && result.length == 0) {
						tdHtml += "&nbsp;";
					}
					tdHtml += "</td>";
				}
				
				return tdHtml;
			},//重构数据行td----end
			/**
			 * 重构表头
			 */
			toFirstTdHtml = function($tdField,i){
				var tdHtml = "";
				var thAttrs = {};
				thAttrs.upImg = "<img border=\"0\" src='../../share/images/view/up.gif'/>";
				thAttrs.downImg = "<img border=\"0\" src='../../share/images/view/down.gif'/>";
				
				thAttrs.colName = $tdField.attr("colName");
				thAttrs.colText = $tdField.attr("colText");
				thAttrs.isVisible = $tdField.attr("isVisible");
				thAttrs.isHiddenColumn = $tdField.attr("isHiddenColumn");
				thAttrs.colWidth = $tdField.attr("colWidth");
				thAttrs.colType = $tdField.attr("colType");
				thAttrs.colFieldName = $tdField.attr("colFieldName");
				thAttrs.isOrderByField = $tdField.attr("isOrderByField");
				thAttrs.isVisible = (thAttrs.isVisible == "true")?true:false;
				thAttrs.isHiddenColumn = (thAttrs.isHiddenColumn == "true")?true:false;
				thAttrs.colWidth = (thAttrs.colWidth == "null") ? "" : thAttrs.colWidth;
				if(thAttrs.isVisible && !thAttrs.isHiddenColumn){
					if(thAttrs.colWidth != "0"){
						if(thAttrs.colWidth != ""){
							isSetWidth = true;
						}
						if(i>2){
							i++;
							tdHtml += "<th isVisible=\"" + thAttrs.isVisible + "\" isHiddenColumn=\"" + thAttrs.isHiddenColumn + "\" class=\"" + Setting.TH_TD_CLASS + "\" data-priority='6'>"+thAttrs.colText+"</th>";
						}else {
							tdHtml += "<th isVisible=\"" + thAttrs.isVisible + "\" isHiddenColumn=\"" + thAttrs.isHiddenColumn + "\" class=\"" + Setting.TH_TD_CLASS + "\" data-priority='1'>"+thAttrs.colText+"</th>";
						}
					}else{
						tdHtml = "<th isVisible=\"" + thAttrs.isVisible + "\" isHiddenColumn=\"" + thAttrs.isHiddenColumn + "\" class=\"" + Setting.TH_TD_CLASS + "\" style=\"display:none;\">" + thAttrs.colName + "</th>";
					}
//				}else{
//					tdHtml = "<th isVisible=\"" + thAttrs.isVisible + "\"  isHiddenColumn=\"" + thAttrs.isHiddenColumn + "\" class=\"" + Setting.TH_TD_CLASS + "\" style=\"display:none;\">" + thAttrs.colName + "</th>";
				}
				return tdHtml;
			};//重构表头----end
			//根据列文本显示方式的配置，截取文本
			truncationText = function(input,displayType,displayLength){
				if(displayType == Column.DISPLAY_PART){
					if(displayLength.match("\\d+")){
						return input.substring(0,displayLength) + "...";
					}
				}
				return input;
			};
			
			var $field = jQuery(this);
			var _sortCol = $field.attr("_sortCol");
			var _sortStatus = $field.attr("_sortStatus");
			var isSum = $field.attr("isSum");
			isSum = (isSum == "true")?true:false;
			var $tableHtml = jQuery("<table data-column-btn-text=\"显示列\" class=\"table-column-toggle ui-responsive table-stroke\" data-role=\"table\" data-mode=\"columntoggle\"></table>");
			var sumTrIsHidden = true;

			//判断是否输出汇总行
			$field.find("#sumTrId").find("td").each(function(){
				if(jQuery(this).attr("isSum") == "true" || jQuery(this).attr("isTotal") == "true"){
					sumTrIsHidden = false;
					return;
				}
			});
			
			$field.children().children().each(function(i){//行<tr>
				var $trHtml = "";
				var $trField = jQuery(this);
				
				if(i == 0){//首行（列头）
					var $head = $("<thead></thead>");
					$trHtml = jQuery("<tr class=\"" + Setting.TH_CLASS + "\"></tr>");
					
					$trField.children().each(function(i){//单元格<td>
						var $tdField = jQuery(this);
						if(i == 0){//首列
							var tdHtml = "";
							tdHtml = "<th class=\"" + Setting.TH_FIRST_TD_CLASS + "\" scope=\"col\"></th>";
							var inputHtml = "";
//								inputHtml += "<input type=\"checkbox\">";
							jQuery(tdHtml).append(jQuery(inputHtml).bind("click",function(){
								selectAll(this.checked);
							})).appendTo($trHtml);
						}else{//其他列
							//根据列头的显示隐藏值设置数据行对应列的显示和隐藏值
							$field.find("tr:gt(0)").each(function(){
								$(this).find(" td:eq("+i+")").attr("isHidden", $tdField.attr("isHiddenColumn"));
							});
							
							$trHtml.append(toFirstTdHtml($tdField,i));
						}
					});
					$tableHtml.append($head.append($trHtml));
					$trHtml = null;
					$head = null;
					
				}else if(isSum && !sumTrIsHidden && (i == $field.children().children().size()-1)){//末行(字段值汇总)
					$trHtml = jQuery("<tr class=\"" + Setting.TR_CLASS + "\" >");
					
					$trField.children().each(function(i){//单元格<td>
						var tdHtml = "";
						var $tdField = jQuery(this);
						var sumTdAttrs = {};
						sumTdAttrs.isVisible = $tdField.attr("isVisible");
						sumTdAttrs.isHiddenColumn = $tdField.attr("isHiddenColumn");
						sumTdAttrs.isSum = $tdField.attr("isSum");
						sumTdAttrs.isTotal = $tdField.attr("isTotal");
						sumTdAttrs.colName = $tdField.attr("colName");
						sumTdAttrs.sumByDatas = $tdField.attr("sumByDatas");
						sumTdAttrs.sumTotal = $tdField.attr("sumTotal");
						sumTdAttrs.isVisible = (sumTdAttrs.isVisible == "true")?true:false;
						sumTdAttrs.isHiddenColumn = (sumTdAttrs.isHiddenColumn == "true")?true:false;
						sumTdAttrs.isSum = (sumTdAttrs.isSum == "true")?true:false;
						sumTdAttrs.isTotal = (sumTdAttrs.isTotal == "true")?true:false;
						if(i == 0){//首列
							tdHtml += "<th class=\"" + Setting.TR_FIRST_TD_CLASS + "\">";
							tdHtml += "&nbsp;</th>";
						}else{//其他列
							if(sumTdAttrs.isVisible && !sumTdAttrs.isHiddenColumn){
								tdHtml += "<td class=\"" + Setting.TR_TD_CLASS + "\" >";
								if(sumTdAttrs.isSum || sumTdAttrs.isTotal)
									tdHtml += sumTdAttrs.colName;
								if(sumTdAttrs.isSum){
									tdHtml += sumTdAttrs.sumByDatas + "&nbsp;";
								}
								if(sumTdAttrs.isTotal){
									tdHtml += sumTdAttrs.sumTotal + "&nbsp;";
								}
								tdHtml += "</td>";
							}
						}
						$trHtml.append(tdHtml);
					});
					$tableHtml.append($trHtml);
					$trHtml = null;
				}else if($trField.attr("trType") =="dataTr"){//数据行
					var dtrHtml = "<tr class=\"" + Setting.TR_CLASS + "\">";
					
					$trField.children().each(function(i){//单元格<td>
						var $tdField = jQuery(this);
						if(i == 0){//首列
							var colId = $tdField.attr("colId");
							var tdHtml =  "<th class=\"" + Setting.TR_FIRST_TD_CLASS + "\">";
								tdHtml += "<input data-enhance='false' type=\"checkbox\" name=\"_selects\" value=\"" + colId + "\"/>";
							
							dtrHtml += tdHtml;
							tdHtml += "</th>";
						}else{//其他列
							dtrHtml += toDataTdHtml($tdField);//重构数据单元格
						}
					});
					
					dtrHtml += "</tr>";
					$tableHtml.append(dtrHtml);
					dtrHtml = "";
				}
			});
			
			var $viewID = $("#view_"+$dvid);
			$tableHtml.attr("id","tableList_"+$dvid);
			$tableHtml.appendTo($viewID);
			$field.remove();
			
			tableListColumn();
			
			if($(".table-view").find(".listDataTr").size()==0){
				$("#content-load-panel").show();
				$("#content-space").css("top",$(window).height()/2-73-10);
			}
		});
	};
})(jQuery);



/**
 * jquery重构列表视图
 * for:列表视图
 */
function jqRefactor4DisplayView(){
	jQuery("table[moduleType='viewList']").obpmDisplayView();
}

function on_delete(colId){
	var rtn = window.confirm("确定要删除您选择的记录吗？");
	if (!rtn){
		return;
	}
	
	var temps = document.getElementsByName("_selects");
	for(var i = 0; i<temps.length; i++){
		if(document.getElementsByName("_selects")[i].value == colId){
			document.getElementsByName("_selects")[i].checked = true;
		}else{
			document.getElementsByName("_selects")[i].checked = false;
		}
	}
	document.forms[0].action = 'delete.action';
	document.forms[0].submit();
}


//子表翻页
function turnPage(act,obj){
	var url = DisplayView.actPagination(act,obj);
	$(obj).parents("div[name='display_view']").load(url, function(){
		var $tabParameter = $(".tab_parameter");
		var viewid = $(this).attr("id") + "_params";
		var $viewDiv = $("<div></div>").attr("id",viewid).attr("_action",$(this).attr("src")).css("display","none");
		//表单元素转成span标签存入dom中
		$.each($tabParameter.find("input[type!=button],select,textarea").serializeArray(),function(){
			$viewDiv.append("<span name='" + this.name + "'>" + this.value + "</span>"); 
		});
		$(this).append($viewDiv);
		//去掉表单元素
		$tabParameter.find("input[type!=button],select,textarea").each(function(){
			if(!$(this).attr("moduleType")) $(this).remove();
		});
		jqRefactor();
	});
}

//===============================重构包含元素去掉iframe改用div--start

/**
* 子视图执行框架
*/
var DisplayView = {
	
	//返回子表翻页url
	actPagination : function(act,obj){
		var $thisTab = $(obj).parents("div[name='display_view']");
		var $pageParameter = $thisTab.find("#"+$thisTab.attr("id") + "_params");
		var $_currpage = parseInt($pageParameter.find("span[name='_currpage']").text()),
			$_pagelines = parseInt($pageParameter.find("span[name='_pagelines']").text()),
			$_rowcount = parseInt($pageParameter.find("span[name='_rowcount']").text());
		var totalPage = Math.ceil($_rowcount/$_pagelines);
		
		switch (act){
		case "showNextPage":
			return $thisTab.attr("src")+"&_currpage="+($_currpage+1);
			break;
		case "showFirstPage":
			return $thisTab.attr("src")+"&_currpage=1";
			break;
		case "showPreviousPage":
			return $thisTab.attr("src")+"&_currpage="+($_currpage-1);
			break;
		case "showLastPage":
			return $thisTab.attr("src")+"&_currpage="+totalPage;
			break;
		default:
			break;
		}	
	},
	
	
	/**
	 * @param obj
	 * @returns 子表视图Div的js对象
	 */
	getTheView : function(obj){
		return $(obj).parents("div[name='display_view']")[0];
	},
	//返回原来form中的action属性值
	getAction : function(obj){
		var view = DisplayView.getTheView(obj);
		return $(view).find("#"+$(view).attr("id") + "_params").attr("_action");
	},
	//获取存在dom中的参数，以json格式返回
	span2Json : function(obj){
		var view = DisplayView.getTheView(obj);
		var json = {};
		$(view).find("#"+$(view).attr("id") + "_params span").each(function(){			
		//$(view).find(".tab_parameter>input").each(function(){
			var name = $(this).attr("name");
			//var val = $(this).val();
			var val = $(this).text();
			if(!json[name] || json[name] == ''){
				json[name] = val;
			}else {
				json[name] += ';' + val;
			}
		});
		return json;
	},
	//在json对象中添加元素值
	addVal2Params : function(json, elems){
		var params = '';
		params = this.json2Params(json);
		$(elems).each(function(){
			var name = this.name;
			var val = this.value;
			if(params == ''){
				params = name + '=' + val;
			}else {
				params += '&' + name + '=' + val;
			}
		});
		return params;
	},
	//获取存在dom中的参数，以params字符串返回
	span2Params : function(obj){
		var view = DisplayView.getTheView(obj);
		var params = '';
		$(view).find("#"+$(view).attr("id") + "_params span").each(function(){
			var name = $(this).attr("name");
			var val = encodeURIComponent($(this).text());
			if(params == ''){
				params = name + '=' + val;
			}else {
				params += '&' + name + '=' + val;
			}
		});
		$(view).find("input[name='_selects']:checked").each(function(){
			var name = $(this).attr("name");
			var val = $(this).val();
			if(params == ''){
				params = name + '=' + val;
			}else {
				params += '&' + name + '=' + val;
			}
		});
		return params;
	},
	//获取存在dom中的参数，以params字符串返回
	refreshSpan : function(obj,name,val){
		var view = DisplayView.getTheView(obj);
		$(view).find("span[name=" + name + "]").each(function(){
			$(this).text(val);
		});
	},
	//把json转换成params字符串
	json2Params : function(json){
		var params = '';
		$.each(json,function(name,val){
			val = encodeURIComponent(val);
			if(params == ''){
				params = name + '=' + val;
			}else {
				params += '&' + name + '=' + val;
			}
		});
		return params;
	},
	//触发重计算
	doCalculation : function(container){
		var isrefreshonchanged = $(container).attr('isrefreshonchanged');
		if(isrefreshonchanged == true || isrefreshonchanged=='true'){
			var name = $(container).attr('getName');
			dy_refresh(name);
		}
	},
	//刷新页面内容
	refreshPage : function(obj,container,html){
		var $html = $("<div></div>").append(html);
		var $form = $html.find("form");
		var _viewid = $form.find('input[name=_viewid]').val();
		var viewid = _viewid + "_params";
		var $viewDiv = $("<div></div>").attr("id",viewid).attr("_action",$form.attr("action")).css("display","none");
		//表单元素转成span标签存入dom中
		$.each($form.serializeArray(),function(){
			$viewDiv.append("<span name='" + this.name + "'>" + this.value + "</span>"); 
		});
		$(container).html($viewDiv);
		//去掉表单元素
		$form.find("input[type!=button],select,textarea").each(function(){
			if(!$(this).attr("moduleType")) $(this).remove();
		});
		//保留form中元素并去掉form
		$form.after($form.html()).remove();
		//插入dom中
		$(container).append($html.html());
		if(typeof(initDispComm)=='function'){
			initDispComm();	//子视图公用初始化方法
			this.doCalculation(container);	//执行刷新重计算方法
		}
	},
	//ajax提交
	postForm : function(obj,url,json){
		$.ajax({
			type : 'POST',
			url : url,
			async : false,
			dataType : 'html',
			data : json,
			context : this.getTheView(obj),
			success : function(html){
				DisplayView.refreshPage(obj,this,html);
			}
		});
	},
	//把json数据转成dom元素(导出Excel时模拟form提交)
	json2Textarea : function(json) {
		var html = '';
		$.each(json,function(name,val){
			html += '<textarea name="'+name+'">'+val+'</textarea>';
		});
		return html;
	},
	//文件下载
	downloadFile : function(url,json){
		$('<form></form>').attr('method','post').attr('action',url).append(this.json2Textarea(json)).submit().remove();
	},
	//打印预览
	printView : function(url,json){
		$('<form></form>').attr('action',url).attr('target','_my_submit_win').append(this.json2Textarea(json)).submit();
	},
	//打开查询页面
	openSearch : function(obj){
		var $div = $('<div id="searchFormTableSub"></div>').append($('#searchFormTable').html());
		artDialog({
			content:$('<div></div>').append($div).html(),
			lock:true,
			width:800,
			height:150,
			okVal:'查询'
		    },
		    function(){
		    	DisplayView.search(obj);
		    },
		    function(){
		    	//取消
		    }
		);
	},
	//查询按钮
	search : function(obj){
		var json = this.span2Json(obj);
		json['_currpage'] = 1;	//重置页码
		//添加控件参数
		$(document).find("#searchFormTableSub").find("input,select,textarea").each(function(){
			json[this.name] = this.value;
		});
		var url = contextPath + "/portal/dynaform/view/subFormView.action?isQueryButton=true";
		this.postForm(obj,url,json);
	},
	//重置按钮
	reset : function(obj){
		var json = this.span2Json(obj);
		var $div = $('<div></div>').append($('#searchFormTable').html());
		$div.find("input,select,textarea").each(function(){
			json[this.name] = '';
		});
		json['_currpage'] = 1;	//重置页码
		var url = contextPath + "/portal/dynaform/view/subFormView.action?isQueryButton=true";
		this.postForm(obj,url,json);
	},
	// 改变排序状态
	changeStatus : function(json, oCol, nCol) {
		if (oCol != nCol && oCol.toUpperCase() != nCol.toUpperCase()) {
			json['_sortStatus'] = "ASC";
			json['_sortCol'] = nCol;
		} else {
			if (json['_sortStatus'] == "ASC") {
				json['_sortStatus'] = "DESC";
				json['_sortCol'] = nCol;
			} 
			else {
				json['_sortStatus'] = "ASC";
				json['_sortCol'] = nCol;
			}
		}
		return json;
	},
	//单击列头排序
	sortTable : function(obj,nColName) {
		var json = this.span2Json(obj);
		var oColName = json['_sortCol'];
		json = this.changeStatus(json, oColName, nColName);
		var url = this.getAction(obj);
		this.postForm(obj,url,json);
	},
	//设置按钮为灰色且不能再操作
	toggleButton : function(obj,btnName) {
		var theBody = this.getTheBody(obj);
		var isBreak = false;
		var button_acts = $(theBody).find('[name=btnName]');
		$(theBody).find('[name=btnName]').each(function(){
			if($(this).attr("isLoad") == "false"){
				isBreak = true;
			}else{
				$(this).attr('isLoad','false');
				$(this).css('color','gray');
			}
		});
		return isBreak;
	},
	//获取选中的行数据
	getCheckedListStr : function(obj,fldName) {
		var view = this.getTheView(obj);
		var rtn = '';
		var flds = $(view).find('[name='+fldName+']');
		$(view).find('[name='+fldName+']').each(function(){
			if($(this).attr('type') == 'checkbox' || $(this).attr('type') == 'radio'){
				if (this.checked && this.value) {
					if(rtn==''){
						rtn += this.value;
					}else {
						rtn += ';' + this.value;
					}
				}
			}else{
				rtn = this.value;
			}
		});
		return rtn;
	},
	//获取选中值
	getValuesMap : function(obj,json) {
		var valuesMap = {};
		var mapVal = "";
		var mapVals = new Array();
		var view = this.getTheView(obj);
		if ($(view).find('[name=_selects]')) {
			var selects = this.getCheckedListStr(obj,'_selects');
			json['_selectsText'] = selects ? selects : '';
		}
		return this.json2Params(json);
	},
	addSearchFormParams : function(obj,json){
		var view = this.getTheView(obj);
		$(view).find("#searchFormTable").find("input,select,textarea").each(function(){
			json[this.name] = this.value;
		});
		return json;
	},
	//重置backurl
	resetBackURL : function(obj,_backURL){
		var view = this.getTheView(obj);
		//添加控件参数
		var isHasAdd = false;	//url是否已添加参数
		if(_backURL!='' && (_backURL.indexOf('&')>=0 || _backURL.indexOf('?')>=0)){
			isHasAdd = true;
		}
		$(view).find("#searchFormTable").find("input,select,textarea").each(function(){
			if(isHasAdd){
				_backURL += "&";
			}else{
				_backURL += "?";
				isHasAdd = true;
			}
			_backURL += this.name + "=" + this.value;;
		});

		return _backURL;
	},
	//获取选中的列表数据
	getSelects : function(view){
		var oSelects = $(view).find("[name='_selects']");
		var _selects="";
		if(oSelects){
			for(var i=0;i<oSelects.length;i++){
				if(oSelects[i].checked){
					_selects+="&_selects="+oSelects[i].value;
				}
			}
		}
		return _selects;
	},
	//查看数据子方法
	viewDocSub : function(obj,action, title, viewType, actid) {
		var openType = OPEN_TYPE_DIV;	//全部使用弹出层方式打开
		var view = this.getTheView(obj);
		var flag = false;
		var _action = this.getAction(obj);
		var json = this.span2Json(obj);
		//执行前脚本
		if(actid){
			jQuery.ajax({
				type: 'POST',
				async:false, 
				url: contextPath + '/portal/dynaform/activity/runbeforeactionscript.action?_actid=' + actid,
				dataType : 'text',
				timeout: 3000,
				data: json,
				success:function(result) {
					if(result != null && result != ""){
			        	var jsmessage = eval("(" + result + ")");
			        	var type = jsmessage.type;
			        	var content = jsmessage.content;
			        	
			        	if(type == '16'){
			        		this.postForm(obj,_action,json);
			        	}
			        	
			        	if(type == '32'){
			        		var rtn = window.confirm(content);
			        		if(!rtn){
				        		this.postForm(obj,_action,json);
			        		}else {
			        			flag = true;
			        		}
			        	}
			        }else {
			        	flag = true;
			        }
				},
				error: function(result) {
					alert("运行脚本出错");
				}
			});
		} else {
			flag = true;
		}

		if(flag){
			var url = action;
		
			if (viewType == VIEW_TYPE_SUB) {
				url += "&isSubDoc=true";
			}
			
			json['_backURL'] = this.resetBackURL(obj,json['_backURL']);	//添加查询表单中的参数
			this.addSearchFormParams(obj,json);	//添加查询表单中的参数
			
			var _json = json;
			var _backURL = json['_backURL'];
			delete json['_backURL'];
			
			var parameters = this.getValuesMap(obj,json);
		
			if (openType == OPEN_TYPE_DIV) {
				var oSelects = $(view).find("[name='_selects']");
				var _selects="";
				if(oSelects){
					for(var i=0;i<oSelects.length;i++){
						if(oSelects[i].checked){
							_selects+="&_selects="+oSelects[i].value;
						}
					}
				}
				url += "&" + parameters + "&openType=" + openType+_selects;
//				url = appendApplicationidByView(url);	//不再需要
				var w = document.body.clientWidth - 25;
				showfrontframe({
							title : "",
							url : url,
							w : w,
							h : 30,
							windowObj : window.parent,
							callback : function(result) {
								setTimeout(function(){//通过右上角的关闭图标关闭弹出层后会显示加载进度条，加延迟后没有这个问题
									_json['_backURL'] = _backURL;
					        		DisplayView.postForm(obj,_action,_json);
								},1);
							}
						});
			}
		}
	},
	//查看数据
	viewDoc : function(obj,docid, formid ,signatureExist,templateForm,isEdit,instanceId,nodeId) {
		// 查看/script/view.js
		var url = docviewAction;
		url += '?_docid=' + docid;
		if (formid) {
			url += '&_formid=' +  formid;
		}
		if (templateForm) {
			url += '&_templateForm=' +  templateForm;
		}
//		if(signatureExist){
//			url += '&signatureExist=' +  signatureExist;
//		}
		if(isedit){
			url += '&isedit=' +  isedit+"";
			//url += '&show_act=' +  isedit+"";
			
		}
		if(instanceId){
			url += '&_targetInstance=' +  instanceId;
		}
		if(nodeId){
			url += '&_targetNode=' +  nodeId;
		}
		this.viewDocSub(obj,url,selectStr, VIEW_TYPE_SUB);
	}
};


//===============================重构包含元素去掉iframe改用div--end; 
!function(){"use strict";function e(e){e.fn.swiper=function(a){var r;return e(this).each(function(){var e=new t(this,a);r||(r=e)}),r}}var a,t=function(e,s){function i(){return"horizontal"===w.params.direction}function n(e){return Math.floor(e)}function o(){w.autoplayTimeoutId=setTimeout(function(){w.params.loop?(w.fixLoop(),w._slideNext()):w.isEnd?s.autoplayStopOnLast?w.stopAutoplay():w._slideTo(0):w._slideNext()},w.params.autoplay)}function l(e,t){var r=a(e.target);if(!r.is(t))if("string"==typeof t)r=r.parents(t);else if(t.nodeType){var s;return r.parents().each(function(e,a){a===t&&(s=t)}),s?t:void 0}return 0===r.length?void 0:r[0]}function d(e,a){a=a||{};var t=window.MutationObserver||window.WebkitMutationObserver,r=new t(function(e){e.forEach(function(e){w.onResize(!0),w.emit("onObserverUpdate",w,e)})});r.observe(e,{attributes:"undefined"==typeof a.attributes?!0:a.attributes,childList:"undefined"==typeof a.childList?!0:a.childList,characterData:"undefined"==typeof a.characterData?!0:a.characterData}),w.observers.push(r)}function p(e){e.originalEvent&&(e=e.originalEvent);var a=e.keyCode||e.charCode;if(!w.params.allowSwipeToNext&&(i()&&39===a||!i()&&40===a))return!1;if(!w.params.allowSwipeToPrev&&(i()&&37===a||!i()&&38===a))return!1;if(!(e.shiftKey||e.altKey||e.ctrlKey||e.metaKey||document.activeElement&&document.activeElement.nodeName&&("input"===document.activeElement.nodeName.toLowerCase()||"textarea"===document.activeElement.nodeName.toLowerCase()))){if(37===a||39===a||38===a||40===a){var t=!1;if(w.container.parents(".swiper-slide").length>0&&0===w.container.parents(".swiper-slide-active").length)return;var r={left:window.pageXOffset,top:window.pageYOffset},s=window.innerWidth,n=window.innerHeight,o=w.container.offset();w.rtl&&(o.left=o.left-w.container[0].scrollLeft);for(var l=[[o.left,o.top],[o.left+w.width,o.top],[o.left,o.top+w.height],[o.left+w.width,o.top+w.height]],d=0;d<l.length;d++){var p=l[d];p[0]>=r.left&&p[0]<=r.left+s&&p[1]>=r.top&&p[1]<=r.top+n&&(t=!0)}if(!t)return}i()?((37===a||39===a)&&(e.preventDefault?e.preventDefault():e.returnValue=!1),(39===a&&!w.rtl||37===a&&w.rtl)&&w.slideNext(),(37===a&&!w.rtl||39===a&&w.rtl)&&w.slidePrev()):((38===a||40===a)&&(e.preventDefault?e.preventDefault():e.returnValue=!1),40===a&&w.slideNext(),38===a&&w.slidePrev())}}function u(e){e.originalEvent&&(e=e.originalEvent);var a=w.mousewheel.event,t=0;if(e.detail)t=-e.detail;else if("mousewheel"===a)if(w.params.mousewheelForceToAxis)if(i()){if(!(Math.abs(e.wheelDeltaX)>Math.abs(e.wheelDeltaY)))return;t=e.wheelDeltaX}else{if(!(Math.abs(e.wheelDeltaY)>Math.abs(e.wheelDeltaX)))return;t=e.wheelDeltaY}else t=e.wheelDelta;else if("DOMMouseScroll"===a)t=-e.detail;else if("wheel"===a)if(w.params.mousewheelForceToAxis)if(i()){if(!(Math.abs(e.deltaX)>Math.abs(e.deltaY)))return;t=-e.deltaX}else{if(!(Math.abs(e.deltaY)>Math.abs(e.deltaX)))return;t=-e.deltaY}else t=Math.abs(e.deltaX)>Math.abs(e.deltaY)?-e.deltaX:-e.deltaY;if(w.params.mousewheelInvert&&(t=-t),w.params.freeMode){var r=w.getWrapperTranslate()+t;if(r>0&&(r=0),r<w.maxTranslate()&&(r=w.maxTranslate()),w.setWrapperTransition(0),w.setWrapperTranslate(r),w.updateProgress(),w.updateActiveIndex(),w.params.freeModeSticky&&(clearTimeout(w.mousewheel.timeout),w.mousewheel.timeout=setTimeout(function(){w.slideReset()},300)),0===r||r===w.maxTranslate())return}else{if((new window.Date).getTime()-w.mousewheel.lastScrollTime>60)if(0>t)if(w.isEnd){if(w.params.mousewheelReleaseOnEdges)return!0}else w.slideNext();else if(w.isBeginning){if(w.params.mousewheelReleaseOnEdges)return!0}else w.slidePrev();w.mousewheel.lastScrollTime=(new window.Date).getTime()}return w.params.autoplay&&w.stopAutoplay(),e.preventDefault?e.preventDefault():e.returnValue=!1,!1}function c(e,t){e=a(e);var r,s,n;r=e.attr("data-swiper-parallax")||"0",s=e.attr("data-swiper-parallax-x"),n=e.attr("data-swiper-parallax-y"),s||n?(s=s||"0",n=n||"0"):i()?(s=r,n="0"):(n=r,s="0"),s=s.indexOf("%")>=0?parseInt(s,10)*t+"%":s*t+"px",n=n.indexOf("%")>=0?parseInt(n,10)*t+"%":n*t+"px",e.transform("translate3d("+s+", "+n+",0px)")}function m(e){return 0!==e.indexOf("on")&&(e=e[0]!==e[0].toUpperCase()?"on"+e[0].toUpperCase()+e.substring(1):"on"+e),e}if(!(this instanceof t))return new t(e,s);var f={direction:"horizontal",touchEventsTarget:"container",initialSlide:0,speed:300,autoplay:!1,autoplayDisableOnInteraction:!0,freeMode:!1,freeModeMomentum:!0,freeModeMomentumRatio:1,freeModeMomentumBounce:!0,freeModeMomentumBounceRatio:1,freeModeSticky:!1,setWrapperSize:!1,virtualTranslate:!1,effect:"slide",coverflow:{rotate:50,stretch:0,depth:100,modifier:1,slideShadows:!0},cube:{slideShadows:!0,shadow:!0,shadowOffset:20,shadowScale:.94},fade:{crossFade:!1},parallax:!1,scrollbar:null,scrollbarHide:!0,keyboardControl:!1,mousewheelControl:!1,mousewheelReleaseOnEdges:!1,mousewheelInvert:!1,mousewheelForceToAxis:!1,hashnav:!1,spaceBetween:0,slidesPerView:1,slidesPerColumn:1,slidesPerColumnFill:"column",slidesPerGroup:1,centeredSlides:!1,slidesOffsetBefore:0,slidesOffsetAfter:0,roundLengths:!1,touchRatio:1,touchAngle:45,simulateTouch:!0,shortSwipes:!0,longSwipes:!0,longSwipesRatio:.5,longSwipesMs:300,followFinger:!0,onlyExternal:!1,threshold:0,touchMoveStopPropagation:!0,pagination:null,paginationElement:"span",paginationClickable:!1,paginationHide:!1,paginationBulletRender:null,resistance:!0,resistanceRatio:.85,nextButton:null,prevButton:null,watchSlidesProgress:!1,watchSlidesVisibility:!1,grabCursor:!1,preventClicks:!0,preventClicksPropagation:!0,slideToClickedSlide:!1,lazyLoading:!1,lazyLoadingInPrevNext:!1,lazyLoadingOnTransitionStart:!1,preloadImages:!0,updateOnImagesReady:!0,loop:!1,loopAdditionalSlides:0,loopedSlides:null,control:void 0,controlInverse:!1,controlBy:"slide",allowSwipeToPrev:!0,allowSwipeToNext:!0,swipeHandler:null,noSwiping:!0,noSwipingClass:"swiper-no-swiping",slideClass:"swiper-slide",slideActiveClass:"swiper-slide-active",slideVisibleClass:"swiper-slide-visible",slideDuplicateClass:"swiper-slide-duplicate",slideNextClass:"swiper-slide-next",slidePrevClass:"swiper-slide-prev",wrapperClass:"swiper-wrapper",bulletClass:"swiper-pagination-bullet",bulletActiveClass:"swiper-pagination-bullet-active",buttonDisabledClass:"swiper-button-disabled",paginationHiddenClass:"swiper-pagination-hidden",observer:!1,observeParents:!1,a11y:!1,prevSlideMessage:"Previous slide",nextSlideMessage:"Next slide",firstSlideMessage:"This is the first slide",lastSlideMessage:"This is the last slide",paginationBulletMessage:"Go to slide {{index}}",runCallbacksOnInit:!0},h=s&&s.virtualTranslate;s=s||{};for(var g in f)if("undefined"==typeof s[g])s[g]=f[g];else if("object"==typeof s[g])for(var v in f[g])"undefined"==typeof s[g][v]&&(s[g][v]=f[g][v]);var w=this;if(w.version="3.1.0",w.params=s,w.classNames=[],"undefined"!=typeof a&&"undefined"!=typeof r&&(a=r),("undefined"!=typeof a||(a="undefined"==typeof r?window.Dom7||window.Zepto||window.jQuery:r))&&(w.$=a,w.container=a(e),0!==w.container.length)){if(w.container.length>1)return void w.container.each(function(){new t(this,s)});w.container[0].swiper=w,w.container.data("swiper",w),w.classNames.push("swiper-container-"+w.params.direction),w.params.freeMode&&w.classNames.push("swiper-container-free-mode"),w.support.flexbox||(w.classNames.push("swiper-container-no-flexbox"),w.params.slidesPerColumn=1),(w.params.parallax||w.params.watchSlidesVisibility)&&(w.params.watchSlidesProgress=!0),["cube","coverflow"].indexOf(w.params.effect)>=0&&(w.support.transforms3d?(w.params.watchSlidesProgress=!0,w.classNames.push("swiper-container-3d")):w.params.effect="slide"),"slide"!==w.params.effect&&w.classNames.push("swiper-container-"+w.params.effect),"cube"===w.params.effect&&(w.params.resistanceRatio=0,w.params.slidesPerView=1,w.params.slidesPerColumn=1,w.params.slidesPerGroup=1,w.params.centeredSlides=!1,w.params.spaceBetween=0,w.params.virtualTranslate=!0,w.params.setWrapperSize=!1),"fade"===w.params.effect&&(w.params.slidesPerView=1,w.params.slidesPerColumn=1,w.params.slidesPerGroup=1,w.params.watchSlidesProgress=!0,w.params.spaceBetween=0,"undefined"==typeof h&&(w.params.virtualTranslate=!0)),w.params.grabCursor&&w.support.touch&&(w.params.grabCursor=!1),w.wrapper=w.container.children("."+w.params.wrapperClass),w.params.pagination&&(w.paginationContainer=a(w.params.pagination),w.params.paginationClickable&&w.paginationContainer.addClass("swiper-pagination-clickable")),w.rtl=i()&&("rtl"===w.container[0].dir.toLowerCase()||"rtl"===w.container.css("direction")),w.rtl&&w.classNames.push("swiper-container-rtl"),w.rtl&&(w.wrongRTL="-webkit-box"===w.wrapper.css("display")),w.params.slidesPerColumn>1&&w.classNames.push("swiper-container-multirow"),w.device.android&&w.classNames.push("swiper-container-android"),w.container.addClass(w.classNames.join(" ")),w.translate=0,w.progress=0,w.velocity=0,w.lockSwipeToNext=function(){w.params.allowSwipeToNext=!1},w.lockSwipeToPrev=function(){w.params.allowSwipeToPrev=!1},w.lockSwipes=function(){w.params.allowSwipeToNext=w.params.allowSwipeToPrev=!1},w.unlockSwipeToNext=function(){w.params.allowSwipeToNext=!0},w.unlockSwipeToPrev=function(){w.params.allowSwipeToPrev=!0},w.unlockSwipes=function(){w.params.allowSwipeToNext=w.params.allowSwipeToPrev=!0},w.params.grabCursor&&(w.container[0].style.cursor="move",w.container[0].style.cursor="-webkit-grab",w.container[0].style.cursor="-moz-grab",w.container[0].style.cursor="grab"),w.imagesToLoad=[],w.imagesLoaded=0,w.loadImage=function(e,a,t,r){function s(){r&&r()}var i;e.complete&&t?s():a?(i=new window.Image,i.onload=s,i.onerror=s,i.src=a):s()},w.preloadImages=function(){function e(){"undefined"!=typeof w&&null!==w&&(void 0!==w.imagesLoaded&&w.imagesLoaded++,w.imagesLoaded===w.imagesToLoad.length&&(w.params.updateOnImagesReady&&w.update(),w.emit("onImagesReady",w)))}w.imagesToLoad=w.container.find("img");for(var a=0;a<w.imagesToLoad.length;a++)w.loadImage(w.imagesToLoad[a],w.imagesToLoad[a].currentSrc||w.imagesToLoad[a].getAttribute("src"),!0,e)},w.autoplayTimeoutId=void 0,w.autoplaying=!1,w.autoplayPaused=!1,w.startAutoplay=function(){return"undefined"!=typeof w.autoplayTimeoutId?!1:w.params.autoplay?w.autoplaying?!1:(w.autoplaying=!0,w.emit("onAutoplayStart",w),void o()):!1},w.stopAutoplay=function(e){w.autoplayTimeoutId&&(w.autoplayTimeoutId&&clearTimeout(w.autoplayTimeoutId),w.autoplaying=!1,w.autoplayTimeoutId=void 0,w.emit("onAutoplayStop",w))},w.pauseAutoplay=function(e){w.autoplayPaused||(w.autoplayTimeoutId&&clearTimeout(w.autoplayTimeoutId),w.autoplayPaused=!0,0===e?(w.autoplayPaused=!1,o()):w.wrapper.transitionEnd(function(){w&&(w.autoplayPaused=!1,w.autoplaying?o():w.stopAutoplay())}))},w.minTranslate=function(){return-w.snapGrid[0]},w.maxTranslate=function(){return-w.snapGrid[w.snapGrid.length-1]},w.updateContainerSize=function(){var e,a;e="undefined"!=typeof w.params.width?w.params.width:w.container[0].clientWidth,a="undefined"!=typeof w.params.height?w.params.height:w.container[0].clientHeight,0===e&&i()||0===a&&!i()||(e=e-parseInt(w.container.css("padding-left"),10)-parseInt(w.container.css("padding-right"),10),a=a-parseInt(w.container.css("padding-top"),10)-parseInt(w.container.css("padding-bottom"),10),w.width=e,w.height=a,w.size=i()?w.width:w.height)},w.updateSlidesSize=function(){w.slides=w.wrapper.children("."+w.params.slideClass),w.snapGrid=[],w.slidesGrid=[],w.slidesSizesGrid=[];var e,a=w.params.spaceBetween,t=-w.params.slidesOffsetBefore,r=0,s=0;"string"==typeof a&&a.indexOf("%")>=0&&(a=parseFloat(a.replace("%",""))/100*w.size),w.virtualSize=-a,w.slides.css(w.rtl?{marginLeft:"",marginTop:""}:{marginRight:"",marginBottom:""});var o;w.params.slidesPerColumn>1&&(o=Math.floor(w.slides.length/w.params.slidesPerColumn)===w.slides.length/w.params.slidesPerColumn?w.slides.length:Math.ceil(w.slides.length/w.params.slidesPerColumn)*w.params.slidesPerColumn);var l,d=w.params.slidesPerColumn,p=o/d,u=p-(w.params.slidesPerColumn*p-w.slides.length);for(e=0;e<w.slides.length;e++){l=0;var c=w.slides.eq(e);if(w.params.slidesPerColumn>1){var m,f,h;"column"===w.params.slidesPerColumnFill?(f=Math.floor(e/d),h=e-f*d,(f>u||f===u&&h===d-1)&&++h>=d&&(h=0,f++),m=f+h*o/d,c.css({"-webkit-box-ordinal-group":m,"-moz-box-ordinal-group":m,"-ms-flex-order":m,"-webkit-order":m,order:m})):(h=Math.floor(e/p),f=e-h*p),c.css({"margin-top":0!==h&&w.params.spaceBetween&&w.params.spaceBetween+"px"}).attr("data-swiper-column",f).attr("data-swiper-row",h)}"none"!==c.css("display")&&("auto"===w.params.slidesPerView?(l=i()?c.outerWidth(!0):c.outerHeight(!0),w.params.roundLengths&&(l=n(l))):(l=(w.size-(w.params.slidesPerView-1)*a)/w.params.slidesPerView,w.params.roundLengths&&(l=n(l)),i()?w.slides[e].style.width=l+"px":w.slides[e].style.height=l+"px"),w.slides[e].swiperSlideSize=l,w.slidesSizesGrid.push(l),w.params.centeredSlides?(t=t+l/2+r/2+a,0===e&&(t=t-w.size/2-a),Math.abs(t)<.001&&(t=0),s%w.params.slidesPerGroup===0&&w.snapGrid.push(t),w.slidesGrid.push(t)):(s%w.params.slidesPerGroup===0&&w.snapGrid.push(t),w.slidesGrid.push(t),t=t+l+a),w.virtualSize+=l+a,r=l,s++)}w.virtualSize=Math.max(w.virtualSize,w.size)+w.params.slidesOffsetAfter;var g;if(w.rtl&&w.wrongRTL&&("slide"===w.params.effect||"coverflow"===w.params.effect)&&w.wrapper.css({width:w.virtualSize+w.params.spaceBetween+"px"}),(!w.support.flexbox||w.params.setWrapperSize)&&w.wrapper.css(i()?{width:w.virtualSize+w.params.spaceBetween+"px"}:{height:w.virtualSize+w.params.spaceBetween+"px"}),w.params.slidesPerColumn>1&&(w.virtualSize=(l+w.params.spaceBetween)*o,w.virtualSize=Math.ceil(w.virtualSize/w.params.slidesPerColumn)-w.params.spaceBetween,w.wrapper.css({width:w.virtualSize+w.params.spaceBetween+"px"}),w.params.centeredSlides)){for(g=[],e=0;e<w.snapGrid.length;e++)w.snapGrid[e]<w.virtualSize+w.snapGrid[0]&&g.push(w.snapGrid[e]);w.snapGrid=g}if(!w.params.centeredSlides){for(g=[],e=0;e<w.snapGrid.length;e++)w.snapGrid[e]<=w.virtualSize-w.size&&g.push(w.snapGrid[e]);w.snapGrid=g,Math.floor(w.virtualSize-w.size)>Math.floor(w.snapGrid[w.snapGrid.length-1])&&w.snapGrid.push(w.virtualSize-w.size)}0===w.snapGrid.length&&(w.snapGrid=[0]),0!==w.params.spaceBetween&&w.slides.css(i()?w.rtl?{marginLeft:a+"px"}:{marginRight:a+"px"}:{marginBottom:a+"px"}),w.params.watchSlidesProgress&&w.updateSlidesOffset()},w.updateSlidesOffset=function(){for(var e=0;e<w.slides.length;e++)w.slides[e].swiperSlideOffset=i()?w.slides[e].offsetLeft:w.slides[e].offsetTop},w.updateSlidesProgress=function(e){if("undefined"==typeof e&&(e=w.translate||0),0!==w.slides.length){"undefined"==typeof w.slides[0].swiperSlideOffset&&w.updateSlidesOffset();var a=w.params.centeredSlides?-e+w.size/2:-e;w.rtl&&(a=w.params.centeredSlides?e-w.size/2:e);{w.container[0].getBoundingClientRect(),i()?"left":"top",i()?"right":"bottom"}w.slides.removeClass(w.params.slideVisibleClass);for(var t=0;t<w.slides.length;t++){var r=w.slides[t],s=w.params.centeredSlides===!0?r.swiperSlideSize/2:0,n=(a-r.swiperSlideOffset-s)/(r.swiperSlideSize+w.params.spaceBetween);if(w.params.watchSlidesVisibility){var o=-(a-r.swiperSlideOffset-s),l=o+w.slidesSizesGrid[t],d=o>=0&&o<w.size||l>0&&l<=w.size||0>=o&&l>=w.size;d&&w.slides.eq(t).addClass(w.params.slideVisibleClass)}r.progress=w.rtl?-n:n}}},w.updateProgress=function(e){"undefined"==typeof e&&(e=w.translate||0);var a=w.maxTranslate()-w.minTranslate();0===a?(w.progress=0,w.isBeginning=w.isEnd=!0):(w.progress=(e-w.minTranslate())/a,w.isBeginning=w.progress<=0,w.isEnd=w.progress>=1),w.isBeginning&&w.emit("onReachBeginning",w),w.isEnd&&w.emit("onReachEnd",w),w.params.watchSlidesProgress&&w.updateSlidesProgress(e),w.emit("onProgress",w,w.progress)},w.updateActiveIndex=function(){var e,a,t,r=w.rtl?w.translate:-w.translate;for(a=0;a<w.slidesGrid.length;a++)"undefined"!=typeof w.slidesGrid[a+1]?r>=w.slidesGrid[a]&&r<w.slidesGrid[a+1]-(w.slidesGrid[a+1]-w.slidesGrid[a])/2?e=a:r>=w.slidesGrid[a]&&r<w.slidesGrid[a+1]&&(e=a+1):r>=w.slidesGrid[a]&&(e=a);(0>e||"undefined"==typeof e)&&(e=0),t=Math.floor(e/w.params.slidesPerGroup),t>=w.snapGrid.length&&(t=w.snapGrid.length-1),e!==w.activeIndex&&(w.snapIndex=t,w.previousIndex=w.activeIndex,w.activeIndex=e,w.updateClasses())},w.updateClasses=function(){w.slides.removeClass(w.params.slideActiveClass+" "+w.params.slideNextClass+" "+w.params.slidePrevClass);var e=w.slides.eq(w.activeIndex);if(e.addClass(w.params.slideActiveClass),e.next("."+w.params.slideClass).addClass(w.params.slideNextClass),e.prev("."+w.params.slideClass).addClass(w.params.slidePrevClass),w.bullets&&w.bullets.length>0){w.bullets.removeClass(w.params.bulletActiveClass);var t;w.params.loop?(t=Math.ceil(w.activeIndex-w.loopedSlides)/w.params.slidesPerGroup,t>w.slides.length-1-2*w.loopedSlides&&(t-=w.slides.length-2*w.loopedSlides),t>w.bullets.length-1&&(t-=w.bullets.length)):t="undefined"!=typeof w.snapIndex?w.snapIndex:w.activeIndex||0,w.paginationContainer.length>1?w.bullets.each(function(){a(this).index()===t&&a(this).addClass(w.params.bulletActiveClass)}):w.bullets.eq(t).addClass(w.params.bulletActiveClass)}w.params.loop||(w.params.prevButton&&(w.isBeginning?(a(w.params.prevButton).addClass(w.params.buttonDisabledClass),w.params.a11y&&w.a11y&&w.a11y.disable(a(w.params.prevButton))):(a(w.params.prevButton).removeClass(w.params.buttonDisabledClass),w.params.a11y&&w.a11y&&w.a11y.enable(a(w.params.prevButton)))),w.params.nextButton&&(w.isEnd?(a(w.params.nextButton).addClass(w.params.buttonDisabledClass),w.params.a11y&&w.a11y&&w.a11y.disable(a(w.params.nextButton))):(a(w.params.nextButton).removeClass(w.params.buttonDisabledClass),w.params.a11y&&w.a11y&&w.a11y.enable(a(w.params.nextButton)))))},w.updatePagination=function(){if(w.params.pagination&&w.paginationContainer&&w.paginationContainer.length>0){for(var e="",a=w.params.loop?Math.ceil((w.slides.length-2*w.loopedSlides)/w.params.slidesPerGroup):w.snapGrid.length,t=0;a>t;t++)e+=w.params.paginationBulletRender?w.params.paginationBulletRender(t,w.params.bulletClass):"<"+w.params.paginationElement+' class="'+w.params.bulletClass+'"></'+w.params.paginationElement+">";w.paginationContainer.html(e),w.bullets=w.paginationContainer.find("."+w.params.bulletClass),w.params.paginationClickable&&w.params.a11y&&w.a11y&&w.a11y.initPagination()}},w.update=function(e){function a(){r=Math.min(Math.max(w.translate,w.maxTranslate()),w.minTranslate()),w.setWrapperTranslate(r),w.updateActiveIndex(),w.updateClasses()}if(w.updateContainerSize(),w.updateSlidesSize(),w.updateProgress(),w.updatePagination(),w.updateClasses(),w.params.scrollbar&&w.scrollbar&&w.scrollbar.set(),e){var t,r;w.controller&&w.controller.spline&&(w.controller.spline=void 0),w.params.freeMode?a():(t=("auto"===w.params.slidesPerView||w.params.slidesPerView>1)&&w.isEnd&&!w.params.centeredSlides?w.slideTo(w.slides.length-1,0,!1,!0):w.slideTo(w.activeIndex,0,!1,!0),t||a())}},w.onResize=function(e){var a=w.params.allowSwipeToPrev,t=w.params.allowSwipeToNext;if(w.params.allowSwipeToPrev=w.params.allowSwipeToNext=!0,w.updateContainerSize(),w.updateSlidesSize(),("auto"===w.params.slidesPerView||w.params.freeMode||e)&&w.updatePagination(),w.params.scrollbar&&w.scrollbar&&w.scrollbar.set(),w.controller&&w.controller.spline&&(w.controller.spline=void 0),w.params.freeMode){var r=Math.min(Math.max(w.translate,w.maxTranslate()),w.minTranslate());w.setWrapperTranslate(r),w.updateActiveIndex(),w.updateClasses()}else w.updateClasses(),("auto"===w.params.slidesPerView||w.params.slidesPerView>1)&&w.isEnd&&!w.params.centeredSlides?w.slideTo(w.slides.length-1,0,!1,!0):w.slideTo(w.activeIndex,0,!1,!0);w.params.allowSwipeToPrev=a,w.params.allowSwipeToNext=t};var y=["mousedown","mousemove","mouseup"];window.navigator.pointerEnabled?y=["pointerdown","pointermove","pointerup"]:window.navigator.msPointerEnabled&&(y=["MSPointerDown","MSPointerMove","MSPointerUp"]),w.touchEvents={start:w.support.touch||!w.params.simulateTouch?"touchstart":y[0],move:w.support.touch||!w.params.simulateTouch?"touchmove":y[1],end:w.support.touch||!w.params.simulateTouch?"touchend":y[2]},(window.navigator.pointerEnabled||window.navigator.msPointerEnabled)&&("container"===w.params.touchEventsTarget?w.container:w.wrapper).addClass("swiper-wp8-"+w.params.direction),w.initEvents=function(e){var t=e?"off":"on",r=e?"removeEventListener":"addEventListener",i="container"===w.params.touchEventsTarget?w.container[0]:w.wrapper[0],n=w.support.touch?i:document,o=w.params.nested?!0:!1;w.browser.ie?(i[r](w.touchEvents.start,w.onTouchStart,!1),n[r](w.touchEvents.move,w.onTouchMove,o),n[r](w.touchEvents.end,w.onTouchEnd,!1)):(w.support.touch&&(i[r](w.touchEvents.start,w.onTouchStart,!1),i[r](w.touchEvents.move,w.onTouchMove,o),i[r](w.touchEvents.end,w.onTouchEnd,!1)),!s.simulateTouch||w.device.ios||w.device.android||(i[r]("mousedown",w.onTouchStart,!1),document[r]("mousemove",w.onTouchMove,o),document[r]("mouseup",w.onTouchEnd,!1))),window[r]("resize",w.onResize),w.params.nextButton&&(a(w.params.nextButton)[t]("click",w.onClickNext),w.params.a11y&&w.a11y&&a(w.params.nextButton)[t]("keydown",w.a11y.onEnterKey)),w.params.prevButton&&(a(w.params.prevButton)[t]("click",w.onClickPrev),w.params.a11y&&w.a11y&&a(w.params.prevButton)[t]("keydown",w.a11y.onEnterKey)),w.params.pagination&&w.params.paginationClickable&&(a(w.paginationContainer)[t]("click","."+w.params.bulletClass,w.onClickIndex),w.params.a11y&&w.a11y&&a(w.paginationContainer)[t]("keydown","."+w.params.bulletClass,w.a11y.onEnterKey)),(w.params.preventClicks||w.params.preventClicksPropagation)&&i[r]("click",w.preventClicks,!0)},w.attachEvents=function(e){w.initEvents()},w.detachEvents=function(){w.initEvents(!0)},w.allowClick=!0,w.preventClicks=function(e){w.allowClick||(w.params.preventClicks&&e.preventDefault(),w.params.preventClicksPropagation&&w.animating&&(e.stopPropagation(),e.stopImmediatePropagation()))},w.onClickNext=function(e){e.preventDefault(),(!w.isEnd||w.params.loop)&&w.slideNext()},w.onClickPrev=function(e){e.preventDefault(),(!w.isBeginning||w.params.loop)&&w.slidePrev()},w.onClickIndex=function(e){e.preventDefault();var t=a(this).index()*w.params.slidesPerGroup;w.params.loop&&(t+=w.loopedSlides),w.slideTo(t)},w.updateClickedSlide=function(e){var t=l(e,"."+w.params.slideClass),r=!1;if(t)for(var s=0;s<w.slides.length;s++)w.slides[s]===t&&(r=!0);if(!t||!r)return w.clickedSlide=void 0,void(w.clickedIndex=void 0);if(w.clickedSlide=t,w.clickedIndex=a(t).index(),w.params.slideToClickedSlide&&void 0!==w.clickedIndex&&w.clickedIndex!==w.activeIndex){var i,n=w.clickedIndex;if(w.params.loop)if(i=a(w.clickedSlide).attr("data-swiper-slide-index"),n>w.slides.length-w.params.slidesPerView)w.fixLoop(),n=w.wrapper.children("."+w.params.slideClass+'[data-swiper-slide-index="'+i+'"]').eq(0).index(),setTimeout(function(){w.slideTo(n)},0);else if(n<w.params.slidesPerView-1){w.fixLoop();var o=w.wrapper.children("."+w.params.slideClass+'[data-swiper-slide-index="'+i+'"]');n=o.eq(o.length-1).index(),setTimeout(function(){w.slideTo(n)},0)}else w.slideTo(n);else w.slideTo(n)}};var b,x,T,S,C,M,E,P,z,I="input, select, textarea, button",k=Date.now(),L=[];w.animating=!1,w.touches={startX:0,startY:0,currentX:0,currentY:0,diff:0};var D,B;if(w.onTouchStart=function(e){if(e.originalEvent&&(e=e.originalEvent),D="touchstart"===e.type,D||!("which"in e)||3!==e.which){if(w.params.noSwiping&&l(e,"."+w.params.noSwipingClass))return void(w.allowClick=!0);if(!w.params.swipeHandler||l(e,w.params.swipeHandler)){if(b=!0,x=!1,S=void 0,B=void 0,w.touches.startX=w.touches.currentX="touchstart"===e.type?e.targetTouches[0].pageX:e.pageX,w.touches.startY=w.touches.currentY="touchstart"===e.type?e.targetTouches[0].pageY:e.pageY,T=Date.now(),w.allowClick=!0,w.updateContainerSize(),w.swipeDirection=void 0,w.params.threshold>0&&(E=!1),"touchstart"!==e.type){var t=!0;a(e.target).is(I)&&(t=!1),document.activeElement&&a(document.activeElement).is(I)&&document.activeElement.blur(),t&&e.preventDefault()}w.emit("onTouchStart",w,e)}}},w.onTouchMove=function(e){if(e.originalEvent&&(e=e.originalEvent),!(D&&"mousemove"===e.type||e.preventedByNestedSwiper)){if(w.params.onlyExternal)return w.allowClick=!1,void(b&&(w.touches.startX=w.touches.currentX="touchmove"===e.type?e.targetTouches[0].pageX:e.pageX,w.touches.startY=w.touches.currentY="touchmove"===e.type?e.targetTouches[0].pageY:e.pageY,T=Date.now()));if(D&&document.activeElement&&e.target===document.activeElement&&a(e.target).is(I))return x=!0,void(w.allowClick=!1);if(w.emit("onTouchMove",w,e),!(e.targetTouches&&e.targetTouches.length>1)){if(w.touches.currentX="touchmove"===e.type?e.targetTouches[0].pageX:e.pageX,w.touches.currentY="touchmove"===e.type?e.targetTouches[0].pageY:e.pageY,"undefined"==typeof S){var t=180*Math.atan2(Math.abs(w.touches.currentY-w.touches.startY),Math.abs(w.touches.currentX-w.touches.startX))/Math.PI;S=i()?t>w.params.touchAngle:90-t>w.params.touchAngle}if(S&&w.emit("onTouchMoveOpposite",w,e),"undefined"==typeof B&&w.browser.ieTouch&&(w.touches.currentX!==w.touches.startX||w.touches.currentY!==w.touches.startY)&&(B=!0),b){if(S)return void(b=!1);if(B||!w.browser.ieTouch){w.allowClick=!1,w.emit("onSliderMove",w,e),e.preventDefault(),w.params.touchMoveStopPropagation&&!w.params.nested&&e.stopPropagation(),x||(s.loop&&w.fixLoop(),M=w.getWrapperTranslate(),w.setWrapperTransition(0),w.animating&&w.wrapper.trigger("webkitTransitionEnd transitionend oTransitionEnd MSTransitionEnd msTransitionEnd"),w.params.autoplay&&w.autoplaying&&(w.params.autoplayDisableOnInteraction?w.stopAutoplay():w.pauseAutoplay()),z=!1,w.params.grabCursor&&(w.container[0].style.cursor="move",w.container[0].style.cursor="-webkit-grabbing",w.container[0].style.cursor="-moz-grabbin",w.container[0].style.cursor="grabbing")),x=!0;var r=w.touches.diff=i()?w.touches.currentX-w.touches.startX:w.touches.currentY-w.touches.startY;r*=w.params.touchRatio,w.rtl&&(r=-r),w.swipeDirection=r>0?"prev":"next",C=r+M;var n=!0;if(r>0&&C>w.minTranslate()?(n=!1,w.params.resistance&&(C=w.minTranslate()-1+Math.pow(-w.minTranslate()+M+r,w.params.resistanceRatio))):0>r&&C<w.maxTranslate()&&(n=!1,w.params.resistance&&(C=w.maxTranslate()+1-Math.pow(w.maxTranslate()-M-r,w.params.resistanceRatio))),n&&(e.preventedByNestedSwiper=!0),!w.params.allowSwipeToNext&&"next"===w.swipeDirection&&M>C&&(C=M),!w.params.allowSwipeToPrev&&"prev"===w.swipeDirection&&C>M&&(C=M),w.params.followFinger){if(w.params.threshold>0){if(!(Math.abs(r)>w.params.threshold||E))return void(C=M);if(!E)return E=!0,w.touches.startX=w.touches.currentX,w.touches.startY=w.touches.currentY,C=M,void(w.touches.diff=i()?w.touches.currentX-w.touches.startX:w.touches.currentY-w.touches.startY)}(w.params.freeMode||w.params.watchSlidesProgress)&&w.updateActiveIndex(),w.params.freeMode&&(0===L.length&&L.push({position:w.touches[i()?"startX":"startY"],time:T}),L.push({position:w.touches[i()?"currentX":"currentY"],time:(new window.Date).getTime()})),w.updateProgress(C),w.setWrapperTranslate(C)}}}}}},w.onTouchEnd=function(e){if(e.originalEvent&&(e=e.originalEvent),w.emit("onTouchEnd",w,e),b){w.params.grabCursor&&x&&b&&(w.container[0].style.cursor="move",w.container[0].style.cursor="-webkit-grab",w.container[0].style.cursor="-moz-grab",w.container[0].style.cursor="grab");var t=Date.now(),r=t-T;if(w.allowClick&&(w.updateClickedSlide(e),w.emit("onTap",w,e),300>r&&t-k>300&&(P&&clearTimeout(P),P=setTimeout(function(){w&&(w.params.paginationHide&&w.paginationContainer.length>0&&!a(e.target).hasClass(w.params.bulletClass)&&w.paginationContainer.toggleClass(w.params.paginationHiddenClass),w.emit("onClick",w,e))},300)),300>r&&300>t-k&&(P&&clearTimeout(P),w.emit("onDoubleTap",w,e))),k=Date.now(),setTimeout(function(){w&&(w.allowClick=!0)},0),!b||!x||!w.swipeDirection||0===w.touches.diff||C===M)return void(b=x=!1);b=x=!1;var s;if(s=w.params.followFinger?w.rtl?w.translate:-w.translate:-C,w.params.freeMode){if(s<-w.minTranslate())return void w.slideTo(w.activeIndex);if(s>-w.maxTranslate())return void w.slideTo(w.slides.length<w.snapGrid.length?w.snapGrid.length-1:w.slides.length-1);if(w.params.freeModeMomentum){if(L.length>1){var i=L.pop(),n=L.pop(),o=i.position-n.position,l=i.time-n.time;w.velocity=o/l,w.velocity=w.velocity/2,Math.abs(w.velocity)<.02&&(w.velocity=0),(l>150||(new window.Date).getTime()-i.time>300)&&(w.velocity=0)}else w.velocity=0;L.length=0;var d=1e3*w.params.freeModeMomentumRatio,p=w.velocity*d,u=w.translate+p;w.rtl&&(u=-u);var c,m=!1,f=20*Math.abs(w.velocity)*w.params.freeModeMomentumBounceRatio;if(u<w.maxTranslate())w.params.freeModeMomentumBounce?(u+w.maxTranslate()<-f&&(u=w.maxTranslate()-f),c=w.maxTranslate(),m=!0,z=!0):u=w.maxTranslate();else if(u>w.minTranslate())w.params.freeModeMomentumBounce?(u-w.minTranslate()>f&&(u=w.minTranslate()+f),c=w.minTranslate(),m=!0,z=!0):u=w.minTranslate();else if(w.params.freeModeSticky){var h,g=0;for(g=0;g<w.snapGrid.length;g+=1)if(w.snapGrid[g]>-u){h=g;break}u=Math.abs(w.snapGrid[h]-u)<Math.abs(w.snapGrid[h-1]-u)||"next"===w.swipeDirection?w.snapGrid[h]:w.snapGrid[h-1],w.rtl||(u=-u)}if(0!==w.velocity)d=Math.abs(w.rtl?(-u-w.translate)/w.velocity:(u-w.translate)/w.velocity);else if(w.params.freeModeSticky)return void w.slideReset();w.params.freeModeMomentumBounce&&m?(w.updateProgress(c),w.setWrapperTransition(d),w.setWrapperTranslate(u),w.onTransitionStart(),w.animating=!0,w.wrapper.transitionEnd(function(){w&&z&&(w.emit("onMomentumBounce",w),w.setWrapperTransition(w.params.speed),w.setWrapperTranslate(c),w.wrapper.transitionEnd(function(){w&&w.onTransitionEnd()}))})):w.velocity?(w.updateProgress(u),w.setWrapperTransition(d),w.setWrapperTranslate(u),w.onTransitionStart(),w.animating||(w.animating=!0,w.wrapper.transitionEnd(function(){w&&w.onTransitionEnd()}))):w.updateProgress(u),w.updateActiveIndex()}return void((!w.params.freeModeMomentum||r>=w.params.longSwipesMs)&&(w.updateProgress(),w.updateActiveIndex()))}var v,y=0,S=w.slidesSizesGrid[0];for(v=0;v<w.slidesGrid.length;v+=w.params.slidesPerGroup)"undefined"!=typeof w.slidesGrid[v+w.params.slidesPerGroup]?s>=w.slidesGrid[v]&&s<w.slidesGrid[v+w.params.slidesPerGroup]&&(y=v,S=w.slidesGrid[v+w.params.slidesPerGroup]-w.slidesGrid[v]):s>=w.slidesGrid[v]&&(y=v,S=w.slidesGrid[w.slidesGrid.length-1]-w.slidesGrid[w.slidesGrid.length-2]);var E=(s-w.slidesGrid[y])/S;if(r>w.params.longSwipesMs){if(!w.params.longSwipes)return void w.slideTo(w.activeIndex);"next"===w.swipeDirection&&w.slideTo(E>=w.params.longSwipesRatio?y+w.params.slidesPerGroup:y),"prev"===w.swipeDirection&&w.slideTo(E>1-w.params.longSwipesRatio?y+w.params.slidesPerGroup:y)}else{if(!w.params.shortSwipes)return void w.slideTo(w.activeIndex);"next"===w.swipeDirection&&w.slideTo(y+w.params.slidesPerGroup),"prev"===w.swipeDirection&&w.slideTo(y)}}},w._slideTo=function(e,a){return w.slideTo(e,a,!0,!0)},w.slideTo=function(e,a,t,r){"undefined"==typeof t&&(t=!0),"undefined"==typeof e&&(e=0),0>e&&(e=0),w.snapIndex=Math.floor(e/w.params.slidesPerGroup),w.snapIndex>=w.snapGrid.length&&(w.snapIndex=w.snapGrid.length-1);var s=-w.snapGrid[w.snapIndex];if(!w.params.allowSwipeToNext&&s<w.translate&&s<w.minTranslate())return!1;if(!w.params.allowSwipeToPrev&&s>w.translate&&s>w.maxTranslate())return!1;w.params.autoplay&&w.autoplaying&&(r||!w.params.autoplayDisableOnInteraction?w.pauseAutoplay(a):w.stopAutoplay()),w.updateProgress(s);for(var n=0;n<w.slidesGrid.length;n++)-Math.floor(100*s)>=Math.floor(100*w.slidesGrid[n])&&(e=n);if("undefined"==typeof a&&(a=w.params.speed),w.previousIndex=w.activeIndex||0,w.activeIndex=e,s===w.translate)return w.updateClasses(),!1;w.updateClasses(),w.onTransitionStart(t);i()?s:0,i()?0:s;return 0===a?(w.setWrapperTransition(0),w.setWrapperTranslate(s),w.onTransitionEnd(t)):(w.setWrapperTransition(a),w.setWrapperTranslate(s),w.animating||(w.animating=!0,w.wrapper.transitionEnd(function(){w&&w.onTransitionEnd(t)}))),!0},w.onTransitionStart=function(e){"undefined"==typeof e&&(e=!0),w.lazy&&w.lazy.onTransitionStart(),e&&(w.emit("onTransitionStart",w),w.activeIndex!==w.previousIndex&&w.emit("onSlideChangeStart",w))},w.onTransitionEnd=function(e){
w.animating=!1,w.setWrapperTransition(0),"undefined"==typeof e&&(e=!0),w.lazy&&w.lazy.onTransitionEnd(),e&&(w.emit("onTransitionEnd",w),w.activeIndex!==w.previousIndex&&w.emit("onSlideChangeEnd",w)),w.params.hashnav&&w.hashnav&&w.hashnav.setHash()},w.slideNext=function(e,a,t){if(w.params.loop){if(w.animating)return!1;w.fixLoop();{w.container[0].clientLeft}return w.slideTo(w.activeIndex+w.params.slidesPerGroup,a,e,t)}return w.slideTo(w.activeIndex+w.params.slidesPerGroup,a,e,t)},w._slideNext=function(e){return w.slideNext(!0,e,!0)},w.slidePrev=function(e,a,t){if(w.params.loop){if(w.animating)return!1;w.fixLoop();{w.container[0].clientLeft}return w.slideTo(w.activeIndex-1,a,e,t)}return w.slideTo(w.activeIndex-1,a,e,t)},w._slidePrev=function(e){return w.slidePrev(!0,e,!0)},w.slideReset=function(e,a,t){return w.slideTo(w.activeIndex,a,e)},w.setWrapperTransition=function(e,a){w.wrapper.transition(e),"slide"!==w.params.effect&&w.effects[w.params.effect]&&w.effects[w.params.effect].setTransition(e),w.params.parallax&&w.parallax&&w.parallax.setTransition(e),w.params.scrollbar&&w.scrollbar&&w.scrollbar.setTransition(e),w.params.control&&w.controller&&w.controller.setTransition(e,a),w.emit("onSetTransition",w,e)},w.setWrapperTranslate=function(e,a,t){var r=0,s=0,n=0;i()?r=w.rtl?-e:e:s=e,w.params.virtualTranslate||w.wrapper.transform(w.support.transforms3d?"translate3d("+r+"px, "+s+"px, "+n+"px)":"translate("+r+"px, "+s+"px)"),w.translate=i()?r:s,a&&w.updateActiveIndex(),"slide"!==w.params.effect&&w.effects[w.params.effect]&&w.effects[w.params.effect].setTranslate(w.translate),w.params.parallax&&w.parallax&&w.parallax.setTranslate(w.translate),w.params.scrollbar&&w.scrollbar&&w.scrollbar.setTranslate(w.translate),w.params.control&&w.controller&&w.controller.setTranslate(w.translate,t),w.emit("onSetTranslate",w,w.translate)},w.getTranslate=function(e,a){var t,r,s,i;return"undefined"==typeof a&&(a="x"),w.params.virtualTranslate?w.rtl?-w.translate:w.translate:(s=window.getComputedStyle(e,null),window.WebKitCSSMatrix?i=new window.WebKitCSSMatrix("none"===s.webkitTransform?"":s.webkitTransform):(i=s.MozTransform||s.OTransform||s.MsTransform||s.msTransform||s.transform||s.getPropertyValue("transform").replace("translate(","matrix(1, 0, 0, 1,"),t=i.toString().split(",")),"x"===a&&(r=window.WebKitCSSMatrix?i.m41:parseFloat(16===t.length?t[12]:t[4])),"y"===a&&(r=window.WebKitCSSMatrix?i.m42:parseFloat(16===t.length?t[13]:t[5])),w.rtl&&r&&(r=-r),r||0)},w.getWrapperTranslate=function(e){return"undefined"==typeof e&&(e=i()?"x":"y"),w.getTranslate(w.wrapper[0],e)},w.observers=[],w.initObservers=function(){if(w.params.observeParents)for(var e=w.container.parents(),a=0;a<e.length;a++)d(e[a]);d(w.container[0],{childList:!1}),d(w.wrapper[0],{attributes:!1})},w.disconnectObservers=function(){for(var e=0;e<w.observers.length;e++)w.observers[e].disconnect();w.observers=[]},w.createLoop=function(){w.wrapper.children("."+w.params.slideClass+"."+w.params.slideDuplicateClass).remove();var e=w.wrapper.children("."+w.params.slideClass);w.loopedSlides=parseInt(w.params.loopedSlides||w.params.slidesPerView,10),w.loopedSlides=w.loopedSlides+w.params.loopAdditionalSlides,w.loopedSlides>e.length&&(w.loopedSlides=e.length);var t,r=[],s=[];for(e.each(function(t,i){var n=a(this);t<w.loopedSlides&&s.push(i),t<e.length&&t>=e.length-w.loopedSlides&&r.push(i),n.attr("data-swiper-slide-index",t)}),t=0;t<s.length;t++)w.wrapper.append(a(s[t].cloneNode(!0)).addClass(w.params.slideDuplicateClass));for(t=r.length-1;t>=0;t--)w.wrapper.prepend(a(r[t].cloneNode(!0)).addClass(w.params.slideDuplicateClass))},w.destroyLoop=function(){w.wrapper.children("."+w.params.slideClass+"."+w.params.slideDuplicateClass).remove(),w.slides.removeAttr("data-swiper-slide-index")},w.fixLoop=function(){var e;w.activeIndex<w.loopedSlides?(e=w.slides.length-3*w.loopedSlides+w.activeIndex,e+=w.loopedSlides,w.slideTo(e,0,!1,!0)):("auto"===w.params.slidesPerView&&w.activeIndex>=2*w.loopedSlides||w.activeIndex>w.slides.length-2*w.params.slidesPerView)&&(e=-w.slides.length+w.activeIndex+w.loopedSlides,e+=w.loopedSlides,w.slideTo(e,0,!1,!0))},w.appendSlide=function(e){if(w.params.loop&&w.destroyLoop(),"object"==typeof e&&e.length)for(var a=0;a<e.length;a++)e[a]&&w.wrapper.append(e[a]);else w.wrapper.append(e);w.params.loop&&w.createLoop(),w.params.observer&&w.support.observer||w.update(!0)},w.prependSlide=function(e){w.params.loop&&w.destroyLoop();var a=w.activeIndex+1;if("object"==typeof e&&e.length){for(var t=0;t<e.length;t++)e[t]&&w.wrapper.prepend(e[t]);a=w.activeIndex+e.length}else w.wrapper.prepend(e);w.params.loop&&w.createLoop(),w.params.observer&&w.support.observer||w.update(!0),w.slideTo(a,0,!1)},w.removeSlide=function(e){w.params.loop&&(w.destroyLoop(),w.slides=w.wrapper.children("."+w.params.slideClass));var a,t=w.activeIndex;if("object"==typeof e&&e.length){for(var r=0;r<e.length;r++)a=e[r],w.slides[a]&&w.slides.eq(a).remove(),t>a&&t--;t=Math.max(t,0)}else a=e,w.slides[a]&&w.slides.eq(a).remove(),t>a&&t--,t=Math.max(t,0);w.params.loop&&w.createLoop(),w.params.observer&&w.support.observer||w.update(!0),w.params.loop?w.slideTo(t+w.loopedSlides,0,!1):w.slideTo(t,0,!1)},w.removeAllSlides=function(){for(var e=[],a=0;a<w.slides.length;a++)e.push(a);w.removeSlide(e)},w.effects={fade:{setTranslate:function(){for(var e=0;e<w.slides.length;e++){var a=w.slides.eq(e),t=a[0].swiperSlideOffset,r=-t;w.params.virtualTranslate||(r-=w.translate);var s=0;i()||(s=r,r=0);var n=w.params.fade.crossFade?Math.max(1-Math.abs(a[0].progress),0):1+Math.min(Math.max(a[0].progress,-1),0);a.css({opacity:n}).transform("translate3d("+r+"px, "+s+"px, 0px)")}},setTransition:function(e){if(w.slides.transition(e),w.params.virtualTranslate&&0!==e){var a=!1;w.slides.transitionEnd(function(){if(!a&&w){a=!0,w.animating=!1;for(var e=["webkitTransitionEnd","transitionend","oTransitionEnd","MSTransitionEnd","msTransitionEnd"],t=0;t<e.length;t++)w.wrapper.trigger(e[t])}})}}},cube:{setTranslate:function(){var e,t=0;w.params.cube.shadow&&(i()?(e=w.wrapper.find(".swiper-cube-shadow"),0===e.length&&(e=a('<div class="swiper-cube-shadow"></div>'),w.wrapper.append(e)),e.css({height:w.width+"px"})):(e=w.container.find(".swiper-cube-shadow"),0===e.length&&(e=a('<div class="swiper-cube-shadow"></div>'),w.container.append(e))));for(var r=0;r<w.slides.length;r++){var s=w.slides.eq(r),n=90*r,o=Math.floor(n/360);w.rtl&&(n=-n,o=Math.floor(-n/360));var l=Math.max(Math.min(s[0].progress,1),-1),d=0,p=0,u=0;r%4===0?(d=4*-o*w.size,u=0):(r-1)%4===0?(d=0,u=4*-o*w.size):(r-2)%4===0?(d=w.size+4*o*w.size,u=w.size):(r-3)%4===0&&(d=-w.size,u=3*w.size+4*w.size*o),w.rtl&&(d=-d),i()||(p=d,d=0);var c="rotateX("+(i()?0:-n)+"deg) rotateY("+(i()?n:0)+"deg) translate3d("+d+"px, "+p+"px, "+u+"px)";if(1>=l&&l>-1&&(t=90*r+90*l,w.rtl&&(t=90*-r-90*l)),s.transform(c),w.params.cube.slideShadows){var m=s.find(i()?".swiper-slide-shadow-left":".swiper-slide-shadow-top"),f=s.find(i()?".swiper-slide-shadow-right":".swiper-slide-shadow-bottom");0===m.length&&(m=a('<div class="swiper-slide-shadow-'+(i()?"left":"top")+'"></div>'),s.append(m)),0===f.length&&(f=a('<div class="swiper-slide-shadow-'+(i()?"right":"bottom")+'"></div>'),s.append(f));{s[0].progress}m.length&&(m[0].style.opacity=-s[0].progress),f.length&&(f[0].style.opacity=s[0].progress)}}if(w.wrapper.css({"-webkit-transform-origin":"50% 50% -"+w.size/2+"px","-moz-transform-origin":"50% 50% -"+w.size/2+"px","-ms-transform-origin":"50% 50% -"+w.size/2+"px","transform-origin":"50% 50% -"+w.size/2+"px"}),w.params.cube.shadow)if(i())e.transform("translate3d(0px, "+(w.width/2+w.params.cube.shadowOffset)+"px, "+-w.width/2+"px) rotateX(90deg) rotateZ(0deg) scale("+w.params.cube.shadowScale+")");else{var h=Math.abs(t)-90*Math.floor(Math.abs(t)/90),g=1.5-(Math.sin(2*h*Math.PI/360)/2+Math.cos(2*h*Math.PI/360)/2),v=w.params.cube.shadowScale,y=w.params.cube.shadowScale/g,b=w.params.cube.shadowOffset;e.transform("scale3d("+v+", 1, "+y+") translate3d(0px, "+(w.height/2+b)+"px, "+-w.height/2/y+"px) rotateX(-90deg)")}var x=w.isSafari||w.isUiWebView?-w.size/2:0;w.wrapper.transform("translate3d(0px,0,"+x+"px) rotateX("+(i()?0:t)+"deg) rotateY("+(i()?-t:0)+"deg)")},setTransition:function(e){w.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e),w.params.cube.shadow&&!i()&&w.container.find(".swiper-cube-shadow").transition(e)}},coverflow:{setTranslate:function(){for(var e=w.translate,t=i()?-e+w.width/2:-e+w.height/2,r=i()?w.params.coverflow.rotate:-w.params.coverflow.rotate,s=w.params.coverflow.depth,n=0,o=w.slides.length;o>n;n++){var l=w.slides.eq(n),d=w.slidesSizesGrid[n],p=l[0].swiperSlideOffset,u=(t-p-d/2)/d*w.params.coverflow.modifier,c=i()?r*u:0,m=i()?0:r*u,f=-s*Math.abs(u),h=i()?0:w.params.coverflow.stretch*u,g=i()?w.params.coverflow.stretch*u:0;Math.abs(g)<.001&&(g=0),Math.abs(h)<.001&&(h=0),Math.abs(f)<.001&&(f=0),Math.abs(c)<.001&&(c=0),Math.abs(m)<.001&&(m=0);var v="translate3d("+g+"px,"+h+"px,"+f+"px)  rotateX("+m+"deg) rotateY("+c+"deg)";if(l.transform(v),l[0].style.zIndex=-Math.abs(Math.round(u))+1,w.params.coverflow.slideShadows){var y=l.find(i()?".swiper-slide-shadow-left":".swiper-slide-shadow-top"),b=l.find(i()?".swiper-slide-shadow-right":".swiper-slide-shadow-bottom");0===y.length&&(y=a('<div class="swiper-slide-shadow-'+(i()?"left":"top")+'"></div>'),l.append(y)),0===b.length&&(b=a('<div class="swiper-slide-shadow-'+(i()?"right":"bottom")+'"></div>'),l.append(b)),y.length&&(y[0].style.opacity=u>0?u:0),b.length&&(b[0].style.opacity=-u>0?-u:0)}}if(w.browser.ie){var x=w.wrapper[0].style;x.perspectiveOrigin=t+"px 50%"}},setTransition:function(e){w.slides.transition(e).find(".swiper-slide-shadow-top, .swiper-slide-shadow-right, .swiper-slide-shadow-bottom, .swiper-slide-shadow-left").transition(e)}}},w.lazy={initialImageLoaded:!1,loadImageInSlide:function(e,t){if("undefined"!=typeof e&&("undefined"==typeof t&&(t=!0),0!==w.slides.length)){var r=w.slides.eq(e),s=r.find(".swiper-lazy:not(.swiper-lazy-loaded):not(.swiper-lazy-loading)");!r.hasClass("swiper-lazy")||r.hasClass("swiper-lazy-loaded")||r.hasClass("swiper-lazy-loading")||s.add(r[0]),0!==s.length&&s.each(function(){var e=a(this);e.addClass("swiper-lazy-loading");var s=e.attr("data-background"),i=e.attr("data-src");w.loadImage(e[0],i||s,!1,function(){if(s?(e.css("background-image","url("+s+")"),e.removeAttr("data-background")):(e.attr("src",i),e.removeAttr("data-src")),e.addClass("swiper-lazy-loaded").removeClass("swiper-lazy-loading"),r.find(".swiper-lazy-preloader, .preloader").remove(),w.params.loop&&t){var a=r.attr("data-swiper-slide-index");if(r.hasClass(w.params.slideDuplicateClass)){var n=w.wrapper.children('[data-swiper-slide-index="'+a+'"]:not(.'+w.params.slideDuplicateClass+")");w.lazy.loadImageInSlide(n.index(),!1)}else{var o=w.wrapper.children("."+w.params.slideDuplicateClass+'[data-swiper-slide-index="'+a+'"]');w.lazy.loadImageInSlide(o.index(),!1)}}w.emit("onLazyImageReady",w,r[0],e[0])}),w.emit("onLazyImageLoad",w,r[0],e[0])})}},load:function(){var e;if(w.params.watchSlidesVisibility)w.wrapper.children("."+w.params.slideVisibleClass).each(function(){w.lazy.loadImageInSlide(a(this).index())});else if(w.params.slidesPerView>1)for(e=w.activeIndex;e<w.activeIndex+w.params.slidesPerView;e++)w.slides[e]&&w.lazy.loadImageInSlide(e);else w.lazy.loadImageInSlide(w.activeIndex);if(w.params.lazyLoadingInPrevNext)if(w.params.slidesPerView>1){for(e=w.activeIndex+w.params.slidesPerView;e<w.activeIndex+w.params.slidesPerView+w.params.slidesPerView;e++)w.slides[e]&&w.lazy.loadImageInSlide(e);for(e=w.activeIndex-w.params.slidesPerView;e<w.activeIndex;e++)w.slides[e]&&w.lazy.loadImageInSlide(e)}else{var t=w.wrapper.children("."+w.params.slideNextClass);t.length>0&&w.lazy.loadImageInSlide(t.index());var r=w.wrapper.children("."+w.params.slidePrevClass);r.length>0&&w.lazy.loadImageInSlide(r.index())}},onTransitionStart:function(){w.params.lazyLoading&&(w.params.lazyLoadingOnTransitionStart||!w.params.lazyLoadingOnTransitionStart&&!w.lazy.initialImageLoaded)&&w.lazy.load()},onTransitionEnd:function(){w.params.lazyLoading&&!w.params.lazyLoadingOnTransitionStart&&w.lazy.load()}},w.scrollbar={set:function(){if(w.params.scrollbar){var e=w.scrollbar;e.track=a(w.params.scrollbar),e.drag=e.track.find(".swiper-scrollbar-drag"),0===e.drag.length&&(e.drag=a('<div class="swiper-scrollbar-drag"></div>'),e.track.append(e.drag)),e.drag[0].style.width="",e.drag[0].style.height="",e.trackSize=i()?e.track[0].offsetWidth:e.track[0].offsetHeight,e.divider=w.size/w.virtualSize,e.moveDivider=e.divider*(e.trackSize/w.size),e.dragSize=e.trackSize*e.divider,i()?e.drag[0].style.width=e.dragSize+"px":e.drag[0].style.height=e.dragSize+"px",e.track[0].style.display=e.divider>=1?"none":"",w.params.scrollbarHide&&(e.track[0].style.opacity=0)}},setTranslate:function(){if(w.params.scrollbar){var e,a=w.scrollbar,t=(w.translate||0,a.dragSize);e=(a.trackSize-a.dragSize)*w.progress,w.rtl&&i()?(e=-e,e>0?(t=a.dragSize-e,e=0):-e+a.dragSize>a.trackSize&&(t=a.trackSize+e)):0>e?(t=a.dragSize+e,e=0):e+a.dragSize>a.trackSize&&(t=a.trackSize-e),i()?(a.drag.transform(w.support.transforms3d?"translate3d("+e+"px, 0, 0)":"translateX("+e+"px)"),a.drag[0].style.width=t+"px"):(a.drag.transform(w.support.transforms3d?"translate3d(0px, "+e+"px, 0)":"translateY("+e+"px)"),a.drag[0].style.height=t+"px"),w.params.scrollbarHide&&(clearTimeout(a.timeout),a.track[0].style.opacity=1,a.timeout=setTimeout(function(){a.track[0].style.opacity=0,a.track.transition(400)},1e3))}},setTransition:function(e){w.params.scrollbar&&w.scrollbar.drag.transition(e)}},w.controller={LinearSpline:function(e,a){this.x=e,this.y=a,this.lastIndex=e.length-1;{var t,r;this.x.length}this.interpolate=function(e){return e?(r=s(this.x,e),t=r-1,(e-this.x[t])*(this.y[r]-this.y[t])/(this.x[r]-this.x[t])+this.y[t]):0};var s=function(){var e,a,t;return function(r,s){for(a=-1,e=r.length;e-a>1;)r[t=e+a>>1]<=s?a=t:e=t;return e}}()},getInterpolateFunction:function(e){w.controller.spline||(w.controller.spline=w.params.loop?new w.controller.LinearSpline(w.slidesGrid,e.slidesGrid):new w.controller.LinearSpline(w.snapGrid,e.snapGrid))},setTranslate:function(e,a){function r(a){e=a.rtl&&"horizontal"===a.params.direction?-w.translate:w.translate,"slide"===w.params.controlBy&&(w.controller.getInterpolateFunction(a),i=-w.controller.spline.interpolate(-e)),i&&"container"!==w.params.controlBy||(s=(a.maxTranslate()-a.minTranslate())/(w.maxTranslate()-w.minTranslate()),i=(e-w.minTranslate())*s+a.minTranslate()),w.params.controlInverse&&(i=a.maxTranslate()-i),a.updateProgress(i),a.setWrapperTranslate(i,!1,w),a.updateActiveIndex()}var s,i,n=w.params.control;if(w.isArray(n))for(var o=0;o<n.length;o++)n[o]!==a&&n[o]instanceof t&&r(n[o]);else n instanceof t&&a!==n&&r(n)},setTransition:function(e,a){function r(a){a.setWrapperTransition(e,w),0!==e&&(a.onTransitionStart(),a.wrapper.transitionEnd(function(){i&&(a.params.loop&&"slide"===w.params.controlBy&&a.fixLoop(),a.onTransitionEnd())}))}var s,i=w.params.control;if(w.isArray(i))for(s=0;s<i.length;s++)i[s]!==a&&i[s]instanceof t&&r(i[s]);else i instanceof t&&a!==i&&r(i)}},w.hashnav={init:function(){if(w.params.hashnav){w.hashnav.initialized=!0;var e=document.location.hash.replace("#","");if(e)for(var a=0,t=0,r=w.slides.length;r>t;t++){var s=w.slides.eq(t),i=s.attr("data-hash");if(i===e&&!s.hasClass(w.params.slideDuplicateClass)){var n=s.index();w.slideTo(n,a,w.params.runCallbacksOnInit,!0)}}}},setHash:function(){w.hashnav.initialized&&w.params.hashnav&&(document.location.hash=w.slides.eq(w.activeIndex).attr("data-hash")||"")}},w.disableKeyboardControl=function(){a(document).off("keydown",p)},w.enableKeyboardControl=function(){a(document).on("keydown",p)},w.mousewheel={event:!1,lastScrollTime:(new window.Date).getTime()},w.params.mousewheelControl){if(void 0!==document.onmousewheel&&(w.mousewheel.event="mousewheel"),!w.mousewheel.event)try{new window.WheelEvent("wheel"),w.mousewheel.event="wheel"}catch(G){}w.mousewheel.event||(w.mousewheel.event="DOMMouseScroll")}w.disableMousewheelControl=function(){return w.mousewheel.event?(w.container.off(w.mousewheel.event,u),!0):!1},w.enableMousewheelControl=function(){return w.mousewheel.event?(w.container.on(w.mousewheel.event,u),!0):!1},w.parallax={setTranslate:function(){w.container.children("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function(){c(this,w.progress)}),w.slides.each(function(){var e=a(this);e.find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function(){var a=Math.min(Math.max(e[0].progress,-1),1);c(this,a)})})},setTransition:function(e){"undefined"==typeof e&&(e=w.params.speed),w.container.find("[data-swiper-parallax], [data-swiper-parallax-x], [data-swiper-parallax-y]").each(function(){var t=a(this),r=parseInt(t.attr("data-swiper-parallax-duration"),10)||e;0===e&&(r=0),t.transition(r)})}},w._plugins=[];for(var O in w.plugins){var A=w.plugins[O](w,w.params[O]);A&&w._plugins.push(A)}return w.callPlugins=function(e){for(var a=0;a<w._plugins.length;a++)e in w._plugins[a]&&w._plugins[a][e](arguments[1],arguments[2],arguments[3],arguments[4],arguments[5])},w.emitterEventListeners={},w.emit=function(e){w.params[e]&&w.params[e](arguments[1],arguments[2],arguments[3],arguments[4],arguments[5]);var a;if(w.emitterEventListeners[e])for(a=0;a<w.emitterEventListeners[e].length;a++)w.emitterEventListeners[e][a](arguments[1],arguments[2],arguments[3],arguments[4],arguments[5]);w.callPlugins&&w.callPlugins(e,arguments[1],arguments[2],arguments[3],arguments[4],arguments[5])},w.on=function(e,a){return e=m(e),w.emitterEventListeners[e]||(w.emitterEventListeners[e]=[]),w.emitterEventListeners[e].push(a),w},w.off=function(e,a){var t;if(e=m(e),"undefined"==typeof a)return w.emitterEventListeners[e]=[],w;if(w.emitterEventListeners[e]&&0!==w.emitterEventListeners[e].length){for(t=0;t<w.emitterEventListeners[e].length;t++)w.emitterEventListeners[e][t]===a&&w.emitterEventListeners[e].splice(t,1);return w}},w.once=function(e,a){e=m(e);var t=function(){a(arguments[0],arguments[1],arguments[2],arguments[3],arguments[4]),w.off(e,t)};return w.on(e,t),w},w.a11y={makeFocusable:function(e){return e.attr("tabIndex","0"),e},addRole:function(e,a){return e.attr("role",a),e},addLabel:function(e,a){return e.attr("aria-label",a),e},disable:function(e){return e.attr("aria-disabled",!0),e},enable:function(e){return e.attr("aria-disabled",!1),e},onEnterKey:function(e){13===e.keyCode&&(a(e.target).is(w.params.nextButton)?(w.onClickNext(e),w.a11y.notify(w.isEnd?w.params.lastSlideMessage:w.params.nextSlideMessage)):a(e.target).is(w.params.prevButton)&&(w.onClickPrev(e),w.a11y.notify(w.isBeginning?w.params.firstSlideMessage:w.params.prevSlideMessage)),a(e.target).is("."+w.params.bulletClass)&&a(e.target)[0].click())},liveRegion:a('<span class="swiper-notification" aria-live="assertive" aria-atomic="true"></span>'),notify:function(e){var a=w.a11y.liveRegion;0!==a.length&&(a.html(""),a.html(e))},init:function(){if(w.params.nextButton){var e=a(w.params.nextButton);w.a11y.makeFocusable(e),w.a11y.addRole(e,"button"),w.a11y.addLabel(e,w.params.nextSlideMessage)}if(w.params.prevButton){var t=a(w.params.prevButton);w.a11y.makeFocusable(t),w.a11y.addRole(t,"button"),w.a11y.addLabel(t,w.params.prevSlideMessage)}a(w.container).append(w.a11y.liveRegion)},initPagination:function(){w.params.pagination&&w.params.paginationClickable&&w.bullets&&w.bullets.length&&w.bullets.each(function(){var e=a(this);w.a11y.makeFocusable(e),w.a11y.addRole(e,"button"),w.a11y.addLabel(e,w.params.paginationBulletMessage.replace(/{{index}}/,e.index()+1))})},destroy:function(){w.a11y.liveRegion&&w.a11y.liveRegion.length>0&&w.a11y.liveRegion.remove()}},w.init=function(){w.params.loop&&w.createLoop(),w.updateContainerSize(),w.updateSlidesSize(),w.updatePagination(),w.params.scrollbar&&w.scrollbar&&w.scrollbar.set(),"slide"!==w.params.effect&&w.effects[w.params.effect]&&(w.params.loop||w.updateProgress(),w.effects[w.params.effect].setTranslate()),w.params.loop?w.slideTo(w.params.initialSlide+w.loopedSlides,0,w.params.runCallbacksOnInit):(w.slideTo(w.params.initialSlide,0,w.params.runCallbacksOnInit),0===w.params.initialSlide&&(w.parallax&&w.params.parallax&&w.parallax.setTranslate(),w.lazy&&w.params.lazyLoading&&(w.lazy.load(),w.lazy.initialImageLoaded=!0))),w.attachEvents(),w.params.observer&&w.support.observer&&w.initObservers(),w.params.preloadImages&&!w.params.lazyLoading&&w.preloadImages(),w.params.autoplay&&w.startAutoplay(),w.params.keyboardControl&&w.enableKeyboardControl&&w.enableKeyboardControl(),w.params.mousewheelControl&&w.enableMousewheelControl&&w.enableMousewheelControl(),w.params.hashnav&&w.hashnav&&w.hashnav.init(),w.params.a11y&&w.a11y&&w.a11y.init(),w.emit("onInit",w)},w.cleanupStyles=function(){w.container.removeClass(w.classNames.join(" ")).removeAttr("style"),w.wrapper.removeAttr("style"),w.slides&&w.slides.length&&w.slides.removeClass([w.params.slideVisibleClass,w.params.slideActiveClass,w.params.slideNextClass,w.params.slidePrevClass].join(" ")).removeAttr("style").removeAttr("data-swiper-column").removeAttr("data-swiper-row"),w.paginationContainer&&w.paginationContainer.length&&w.paginationContainer.removeClass(w.params.paginationHiddenClass),w.bullets&&w.bullets.length&&w.bullets.removeClass(w.params.bulletActiveClass),w.params.prevButton&&a(w.params.prevButton).removeClass(w.params.buttonDisabledClass),w.params.nextButton&&a(w.params.nextButton).removeClass(w.params.buttonDisabledClass),w.params.scrollbar&&w.scrollbar&&(w.scrollbar.track&&w.scrollbar.track.length&&w.scrollbar.track.removeAttr("style"),w.scrollbar.drag&&w.scrollbar.drag.length&&w.scrollbar.drag.removeAttr("style"))},w.destroy=function(e,a){w.detachEvents(),w.stopAutoplay(),w.params.loop&&w.destroyLoop(),a&&w.cleanupStyles(),w.disconnectObservers(),w.params.keyboardControl&&w.disableKeyboardControl&&w.disableKeyboardControl(),w.params.mousewheelControl&&w.disableMousewheelControl&&w.disableMousewheelControl(),w.params.a11y&&w.a11y&&w.a11y.destroy(),w.emit("onDestroy"),e!==!1&&(w=null)},w.init(),w}};t.prototype={isSafari:function(){var e=navigator.userAgent.toLowerCase();return e.indexOf("safari")>=0&&e.indexOf("chrome")<0&&e.indexOf("android")<0}(),isUiWebView:/(iPhone|iPod|iPad).*AppleWebKit(?!.*Safari)/i.test(navigator.userAgent),isArray:function(e){return"[object Array]"===Object.prototype.toString.apply(e)},browser:{ie:window.navigator.pointerEnabled||window.navigator.msPointerEnabled,ieTouch:window.navigator.msPointerEnabled&&window.navigator.msMaxTouchPoints>1||window.navigator.pointerEnabled&&window.navigator.maxTouchPoints>1},device:function(){var e=navigator.userAgent,a=e.match(/(Android);?[\s\/]+([\d.]+)?/),t=e.match(/(iPad).*OS\s([\d_]+)/),r=e.match(/(iPod)(.*OS\s([\d_]+))?/),s=!t&&e.match(/(iPhone\sOS)\s([\d_]+)/);return{ios:t||s||r,android:a}}(),support:{touch:window.Modernizr&&Modernizr.touch===!0||function(){return!!("ontouchstart"in window||window.DocumentTouch&&document instanceof DocumentTouch)}(),transforms3d:window.Modernizr&&Modernizr.csstransforms3d===!0||function(){var e=document.createElement("div").style;return"webkitPerspective"in e||"MozPerspective"in e||"OPerspective"in e||"MsPerspective"in e||"perspective"in e}(),flexbox:function(){for(var e=document.createElement("div").style,a="alignItems webkitAlignItems webkitBoxAlign msFlexAlign mozBoxAlign webkitFlexDirection msFlexDirection mozBoxDirection mozBoxOrient webkitBoxDirection webkitBoxOrient".split(" "),t=0;t<a.length;t++)if(a[t]in e)return!0}(),observer:function(){return"MutationObserver"in window||"WebkitMutationObserver"in window}()},plugins:{}};for(var r=(function(){var e=function(e){var a=this,t=0;for(t=0;t<e.length;t++)a[t]=e[t];return a.length=e.length,this},a=function(a,t){var r=[],s=0;if(a&&!t&&a instanceof e)return a;if(a)if("string"==typeof a){var i,n,o=a.trim();if(o.indexOf("<")>=0&&o.indexOf(">")>=0){var l="div";for(0===o.indexOf("<li")&&(l="ul"),0===o.indexOf("<tr")&&(l="tbody"),(0===o.indexOf("<td")||0===o.indexOf("<th"))&&(l="tr"),0===o.indexOf("<tbody")&&(l="table"),0===o.indexOf("<option")&&(l="select"),n=document.createElement(l),n.innerHTML=a,s=0;s<n.childNodes.length;s++)r.push(n.childNodes[s])}else for(i=t||"#"!==a[0]||a.match(/[ .<>:~]/)?(t||document).querySelectorAll(a):[document.getElementById(a.split("#")[1])],s=0;s<i.length;s++)i[s]&&r.push(i[s])}else if(a.nodeType||a===window||a===document)r.push(a);else if(a.length>0&&a[0].nodeType)for(s=0;s<a.length;s++)r.push(a[s]);return new e(r)};return e.prototype={addClass:function(e){if("undefined"==typeof e)return this;for(var a=e.split(" "),t=0;t<a.length;t++)for(var r=0;r<this.length;r++)this[r].classList.add(a[t]);return this},removeClass:function(e){for(var a=e.split(" "),t=0;t<a.length;t++)for(var r=0;r<this.length;r++)this[r].classList.remove(a[t]);return this},hasClass:function(e){return this[0]?this[0].classList.contains(e):!1},toggleClass:function(e){for(var a=e.split(" "),t=0;t<a.length;t++)for(var r=0;r<this.length;r++)this[r].classList.toggle(a[t]);return this},attr:function(e,a){if(1===arguments.length&&"string"==typeof e)return this[0]?this[0].getAttribute(e):void 0;for(var t=0;t<this.length;t++)if(2===arguments.length)this[t].setAttribute(e,a);else for(var r in e)this[t][r]=e[r],this[t].setAttribute(r,e[r]);return this},removeAttr:function(e){for(var a=0;a<this.length;a++)this[a].removeAttribute(e);return this},data:function(e,a){if("undefined"==typeof a){if(this[0]){var t=this[0].getAttribute("data-"+e);return t?t:this[0].dom7ElementDataStorage&&e in this[0].dom7ElementDataStorage?this[0].dom7ElementDataStorage[e]:void 0}return void 0}for(var r=0;r<this.length;r++){var s=this[r];s.dom7ElementDataStorage||(s.dom7ElementDataStorage={}),s.dom7ElementDataStorage[e]=a}return this},transform:function(e){for(var a=0;a<this.length;a++){var t=this[a].style;t.webkitTransform=t.MsTransform=t.msTransform=t.MozTransform=t.OTransform=t.transform=e}return this},transition:function(e){"string"!=typeof e&&(e+="ms");for(var a=0;a<this.length;a++){var t=this[a].style;t.webkitTransitionDuration=t.MsTransitionDuration=t.msTransitionDuration=t.MozTransitionDuration=t.OTransitionDuration=t.transitionDuration=e}return this},on:function(e,t,r,s){function i(e){var s=e.target;if(a(s).is(t))r.call(s,e);else for(var i=a(s).parents(),n=0;n<i.length;n++)a(i[n]).is(t)&&r.call(i[n],e)}var n,o,l=e.split(" ");for(n=0;n<this.length;n++)if("function"==typeof t||t===!1)for("function"==typeof t&&(r=arguments[1],s=arguments[2]||!1),o=0;o<l.length;o++)this[n].addEventListener(l[o],r,s);else for(o=0;o<l.length;o++)this[n].dom7LiveListeners||(this[n].dom7LiveListeners=[]),this[n].dom7LiveListeners.push({listener:r,liveListener:i}),this[n].addEventListener(l[o],i,s);return this},off:function(e,a,t,r){for(var s=e.split(" "),i=0;i<s.length;i++)for(var n=0;n<this.length;n++)if("function"==typeof a||a===!1)"function"==typeof a&&(t=arguments[1],r=arguments[2]||!1),this[n].removeEventListener(s[i],t,r);else if(this[n].dom7LiveListeners)for(var o=0;o<this[n].dom7LiveListeners.length;o++)this[n].dom7LiveListeners[o].listener===t&&this[n].removeEventListener(s[i],this[n].dom7LiveListeners[o].liveListener,r);return this},once:function(e,a,t,r){function s(n){t(n),i.off(e,a,s,r)}var i=this;"function"==typeof a&&(a=!1,t=arguments[1],r=arguments[2]),i.on(e,a,s,r)},trigger:function(e,a){for(var t=0;t<this.length;t++){var r;try{r=new window.CustomEvent(e,{detail:a,bubbles:!0,cancelable:!0})}catch(s){r=document.createEvent("Event"),r.initEvent(e,!0,!0),r.detail=a}this[t].dispatchEvent(r)}return this},transitionEnd:function(e){function a(i){if(i.target===this)for(e.call(this,i),t=0;t<r.length;t++)s.off(r[t],a)}var t,r=["webkitTransitionEnd","transitionend","oTransitionEnd","MSTransitionEnd","msTransitionEnd"],s=this;if(e)for(t=0;t<r.length;t++)s.on(r[t],a);return this},width:function(){return this[0]===window?window.innerWidth:this.length>0?parseFloat(this.css("width")):null},outerWidth:function(e){return this.length>0?e?this[0].offsetWidth+parseFloat(this.css("margin-right"))+parseFloat(this.css("margin-left")):this[0].offsetWidth:null},height:function(){return this[0]===window?window.innerHeight:this.length>0?parseFloat(this.css("height")):null},outerHeight:function(e){return this.length>0?e?this[0].offsetHeight+parseFloat(this.css("margin-top"))+parseFloat(this.css("margin-bottom")):this[0].offsetHeight:null},offset:function(){if(this.length>0){var e=this[0],a=e.getBoundingClientRect(),t=document.body,r=e.clientTop||t.clientTop||0,s=e.clientLeft||t.clientLeft||0,i=window.pageYOffset||e.scrollTop,n=window.pageXOffset||e.scrollLeft;return{top:a.top+i-r,left:a.left+n-s}}return null},css:function(e,a){var t;if(1===arguments.length){if("string"!=typeof e){for(t=0;t<this.length;t++)for(var r in e)this[t].style[r]=e[r];return this}if(this[0])return window.getComputedStyle(this[0],null).getPropertyValue(e)}if(2===arguments.length&&"string"==typeof e){for(t=0;t<this.length;t++)this[t].style[e]=a;return this}return this},each:function(e){for(var a=0;a<this.length;a++)e.call(this[a],a,this[a]);return this},html:function(e){if("undefined"==typeof e)return this[0]?this[0].innerHTML:void 0;for(var a=0;a<this.length;a++)this[a].innerHTML=e;return this},is:function(t){if(!this[0])return!1;var r,s;if("string"==typeof t){var i=this[0];if(i===document)return t===document;if(i===window)return t===window;if(i.matches)return i.matches(t);if(i.webkitMatchesSelector)return i.webkitMatchesSelector(t);if(i.mozMatchesSelector)return i.mozMatchesSelector(t);if(i.msMatchesSelector)return i.msMatchesSelector(t);for(r=a(t),s=0;s<r.length;s++)if(r[s]===this[0])return!0;return!1}if(t===document)return this[0]===document;if(t===window)return this[0]===window;if(t.nodeType||t instanceof e){for(r=t.nodeType?[t]:t,s=0;s<r.length;s++)if(r[s]===this[0])return!0;return!1}return!1},index:function(){if(this[0]){for(var e=this[0],a=0;null!==(e=e.previousSibling);)1===e.nodeType&&a++;return a}return void 0},eq:function(a){if("undefined"==typeof a)return this;var t,r=this.length;return a>r-1?new e([]):0>a?(t=r+a,new e(0>t?[]:[this[t]])):new e([this[a]])},append:function(a){var t,r;for(t=0;t<this.length;t++)if("string"==typeof a){var s=document.createElement("div");for(s.innerHTML=a;s.firstChild;)this[t].appendChild(s.firstChild)}else if(a instanceof e)for(r=0;r<a.length;r++)this[t].appendChild(a[r]);else this[t].appendChild(a);return this},prepend:function(a){var t,r;for(t=0;t<this.length;t++)if("string"==typeof a){var s=document.createElement("div");for(s.innerHTML=a,r=s.childNodes.length-1;r>=0;r--)this[t].insertBefore(s.childNodes[r],this[t].childNodes[0])}else if(a instanceof e)for(r=0;r<a.length;r++)this[t].insertBefore(a[r],this[t].childNodes[0]);else this[t].insertBefore(a,this[t].childNodes[0]);return this},insertBefore:function(e){for(var t=a(e),r=0;r<this.length;r++)if(1===t.length)t[0].parentNode.insertBefore(this[r],t[0]);else if(t.length>1)for(var s=0;s<t.length;s++)t[s].parentNode.insertBefore(this[r].cloneNode(!0),t[s])},insertAfter:function(e){for(var t=a(e),r=0;r<this.length;r++)if(1===t.length)t[0].parentNode.insertBefore(this[r],t[0].nextSibling);else if(t.length>1)for(var s=0;s<t.length;s++)t[s].parentNode.insertBefore(this[r].cloneNode(!0),t[s].nextSibling)},next:function(t){return new e(this.length>0?t?this[0].nextElementSibling&&a(this[0].nextElementSibling).is(t)?[this[0].nextElementSibling]:[]:this[0].nextElementSibling?[this[0].nextElementSibling]:[]:[])},nextAll:function(t){var r=[],s=this[0];if(!s)return new e([]);for(;s.nextElementSibling;){var i=s.nextElementSibling;t?a(i).is(t)&&r.push(i):r.push(i),s=i}return new e(r)},prev:function(t){return new e(this.length>0?t?this[0].previousElementSibling&&a(this[0].previousElementSibling).is(t)?[this[0].previousElementSibling]:[]:this[0].previousElementSibling?[this[0].previousElementSibling]:[]:[]);

},prevAll:function(t){var r=[],s=this[0];if(!s)return new e([]);for(;s.previousElementSibling;){var i=s.previousElementSibling;t?a(i).is(t)&&r.push(i):r.push(i),s=i}return new e(r)},parent:function(e){for(var t=[],r=0;r<this.length;r++)e?a(this[r].parentNode).is(e)&&t.push(this[r].parentNode):t.push(this[r].parentNode);return a(a.unique(t))},parents:function(e){for(var t=[],r=0;r<this.length;r++)for(var s=this[r].parentNode;s;)e?a(s).is(e)&&t.push(s):t.push(s),s=s.parentNode;return a(a.unique(t))},find:function(a){for(var t=[],r=0;r<this.length;r++)for(var s=this[r].querySelectorAll(a),i=0;i<s.length;i++)t.push(s[i]);return new e(t)},children:function(t){for(var r=[],s=0;s<this.length;s++)for(var i=this[s].childNodes,n=0;n<i.length;n++)t?1===i[n].nodeType&&a(i[n]).is(t)&&r.push(i[n]):1===i[n].nodeType&&r.push(i[n]);return new e(a.unique(r))},remove:function(){for(var e=0;e<this.length;e++)this[e].parentNode&&this[e].parentNode.removeChild(this[e]);return this},add:function(){var e,t,r=this;for(e=0;e<arguments.length;e++){var s=a(arguments[e]);for(t=0;t<s.length;t++)r[r.length]=s[t],r.length++}return r}},a.fn=e.prototype,a.unique=function(e){for(var a=[],t=0;t<e.length;t++)-1===a.indexOf(e[t])&&a.push(e[t]);return a},a}()),s=["jQuery","Zepto","Dom7"],i=0;i<s.length;i++)window[s[i]]&&e(window[s[i]]);var n;n="undefined"==typeof r?window.Dom7||window.Zepto||window.jQuery:r,n&&("transitionEnd"in n.fn||(n.fn.transitionEnd=function(e){function a(i){if(i.target===this)for(e.call(this,i),t=0;t<r.length;t++)s.off(r[t],a)}var t,r=["webkitTransitionEnd","transitionend","oTransitionEnd","MSTransitionEnd","msTransitionEnd"],s=this;if(e)for(t=0;t<r.length;t++)s.on(r[t],a);return this}),"transform"in n.fn||(n.fn.transform=function(e){for(var a=0;a<this.length;a++){var t=this[a].style;t.webkitTransform=t.MsTransform=t.msTransform=t.MozTransform=t.OTransform=t.transform=e}return this}),"transition"in n.fn||(n.fn.transition=function(e){"string"!=typeof e&&(e+="ms");for(var a=0;a<this.length;a++){var t=this[a].style;t.webkitTransitionDuration=t.MsTransitionDuration=t.msTransitionDuration=t.MozTransitionDuration=t.OTransitionDuration=t.transitionDuration=e}return this})),window.Swiper=t}(),"undefined"!=typeof module?module.exports=window.Swiper:"function"==typeof define&&define.amd&&define([],function(){"use strict";return window.Swiper});; 
var Common = {};
Common.Util = {
	cache : {
		
	},
	/**
	 * 获取用户头像图片url地址
	 * @param userId
	 */	
	getAvatar : function(userId){
		if(!this.cache[userId]){
			$.ajax({
				type: "GET",
				url: contextPath + "/contacts/getAvatar.action",
				data: {"id":userId},
				async: false,
				dataType: "json",
				success:function(result){
					if(1==result.status){
						Common.Util.cache[userId] = result.data;
					}
				}
			});
		}
		
		return this.cache[userId];;
	},
	/**
	 * 计算时间差
	 * @param date,date2
	 */
	daysCalc : function(date,date2){
		var startDateArr = date.split(/[- :]/); 
		var startDate = new Date(startDateArr[0], startDateArr[1]-1, startDateArr[2], startDateArr[3], startDateArr[4]);
		var nowDate = new Date();
		if(!date2 || date2 == ""){
			var nowDate = new Date();
		}else{
			var endDateArr = date2.split(/[- :]/); 
			nowDate = new Date(endDateArr[0], endDateArr[1]-1, endDateArr[2], endDateArr[3], endDateArr[4]);
		}
		var msDate = nowDate.getTime() - startDate.getTime();
		//计算出相差天数
		var days=Math.floor(msDate/(24*3600*1000));
		//计算出小时数
		var leave1 = msDate%(24*3600*1000);//计算天数后剩余的毫秒数
		var hours = Math.floor(leave1/(3600*1000));
		//计算相差分钟数
		var leave2 = leave1%(3600*1000);//计算小时数后剩余的毫秒数
		var minutes = Math.floor(leave2/(60*1000));
		//计算相差秒数
		var leave3=leave2%(60*1000);//计算分钟数后剩余的毫秒数
		var seconds=Math.round(leave3/1000);
		//alert(" 相差 "+days+"天 "+hours+"小时 "+minutes+" 分钟"+seconds+" 秒");	
		var timeCalc = {
			    "days": days,
			    "hours": hours,
			    "minutes": minutes,
			    "seconds": seconds
			};
		return timeCalc;
	}
}; 
/*!
 * jQuery Cookie Plugin v1.4.1
 * https://github.com/carhartl/jquery-cookie
 *
 * Copyright 2006, 2014 Klaus Hartl
 * Released under the MIT license
 */
(function (factory) {
	if (typeof define === 'function' && define.amd) {
		// AMD (Register as an anonymous module)
		define(['jquery'], factory);
	} else if (typeof exports === 'object') {
		// Node/CommonJS
		module.exports = factory(require('jquery'));
	} else {
		// Browser globals
		factory(jQuery);
	}
}(function ($) {

	var pluses = /\+/g;

	function encode(s) {
		return config.raw ? s : encodeURIComponent(s);
	}

	function decode(s) {
		return config.raw ? s : decodeURIComponent(s);
	}

	function stringifyCookieValue(value) {
		return encode(config.json ? JSON.stringify(value) : String(value));
	}

	function parseCookieValue(s) {
		if (s.indexOf('"') === 0) {
			// This is a quoted cookie as according to RFC2068, unescape...
			s = s.slice(1, -1).replace(/\\"/g, '"').replace(/\\\\/g, '\\');
		}

		try {
			// Replace server-side written pluses with spaces.
			// If we can't decode the cookie, ignore it, it's unusable.
			// If we can't parse the cookie, ignore it, it's unusable.
			s = decodeURIComponent(s.replace(pluses, ' '));
			return config.json ? JSON.parse(s) : s;
		} catch(e) {}
	}

	function read(s, converter) {
		var value = config.raw ? s : parseCookieValue(s);
		return $.isFunction(converter) ? converter(value) : value;
	}

	var config = $.cookie = function (key, value, options) {

		// Write

		if (arguments.length > 1 && !$.isFunction(value)) {
			options = $.extend({}, config.defaults, options);

			if (typeof options.expires === 'number') {
				var days = options.expires, t = options.expires = new Date();
				t.setMilliseconds(t.getMilliseconds() + days * 864e+5);
			}

			return (document.cookie = [
				encode(key), '=', stringifyCookieValue(value),
				options.expires ? '; expires=' + options.expires.toUTCString() : '', // use expires attribute, max-age is not supported by IE
				options.path    ? '; path=' + options.path : '',
				options.domain  ? '; domain=' + options.domain : '',
				options.secure  ? '; secure' : ''
			].join(''));
		}

		// Read

		var result = key ? undefined : {},
			// To prevent the for loop in the first place assign an empty array
			// in case there are no cookies at all. Also prevents odd result when
			// calling $.cookie().
			cookies = document.cookie ? document.cookie.split('; ') : [],
			i = 0,
			l = cookies.length;

		for (; i < l; i++) {
			var parts = cookies[i].split('='),
				name = decode(parts.shift()),
				cookie = parts.join('=');

			if (key === name) {
				// If second argument (value) is a function it's a converter...
				result = read(cookie, value);
				break;
			}

			// Prevent storing a cookie that we couldn't decode.
			if (!key && (cookie = read(cookie)) !== undefined) {
				result[name] = cookie;
			}
		}

		return result;
	};

	config.defaults = {};

	$.removeCookie = function (key, options) {
		// Must not alter options, thus extending a fresh object...
		$.cookie(key, '', $.extend({}, options, { expires: -1 }));
		return !$.cookie(key);
	};

}));
