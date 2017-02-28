!function(e, t) {
	"object" == typeof exports && "object" == typeof module ? module.exports = t() : "function" == typeof define && define.amd ? define("Router", [], t) : "object" == typeof exports ? exports.Router = t() : e.Router = t()
} (this,function() {
	return function(e) {
		function t(o) {
			if (n[o]) return n[o].exports;
			var r = n[o] = {
				exports: {},
				id: o,
				loaded: !1
			};
			return e[o].call(r.exports, r, r.exports, t),
			r.loaded = !0,
			r.exports
		}
		var n = {};
		return t.m = e,
		t.c = n,
		t.p = "",
		t(0)
	} ([function(e, t, n) {
		"use strict";
		function o(e) {
			if (e && e.__esModule) return e;
			var t = {};
			if (null != e) for (var n in e) Object.prototype.hasOwnProperty.call(e, n) && (t[n] = e[n]);
			return t["default"] = e,
			t
		}
		function r(e, t) {
			if (! (e instanceof t)) throw new TypeError("Cannot call a class as a function")
		}
		Object.defineProperty(t, "__esModule", {
			value: !0
		});
		var i = function() {
			function e(e, t) {
				for (var n = 0; n < t.length; n++) {
					var o = t[n];
					o.enumerable = o.enumerable || !1,
					o.configurable = !0,
					"value" in o && (o.writable = !0),
					Object.defineProperty(e, o.key, o)
				}
			}
			return function(t, n, o) {
				return n && e(t.prototype, n),
				o && e(t, o),
				t
			}
		} (),
		s = n(1),
		a = o(s),
		u = function() {
			function e(t) {
				r(this, e),
				this._options = {
					container: "#container",
					enter: "enter",
					enterTimeout: 0,
					leave: "leave",
					leaveTimeout: 0
				},
				this._index = 1,
				this._$container = null,
				this._routes = [],
				this._default = null,
				this._options = a.extend(this._options, t),
				this._$container = document.querySelector(this._options.container)
			}
			return i(e, [{
				key: "init",
				value: function() {
					var e = this;
					window.addEventListener("hashchange",function(t) {
						var n = a.getHash(t.newURL),
						o = history.state || {};
						e.go(n, o._index <= e._index)
					},!1),
					history.state && history.state._index && (this._index = history.state._index),
					this._index--;
					var t = a.getHash(location.href),
					n = this._getRoute(t);
					return this.go(n ? t: this._default),
					this
				}
			},
			{
				key: "push",
				value: function(e) {
					return e = a.extend({
						url: "*",
						className: "",
						render: a.noop,
						bind: a.noop
					},
					e),
					this._routes.push(e),
					this
				}
			},
			{
				key: "setDefault",
				value: function(e) {
					return this._default = e,
					this
				}
			},
			{
				key: "go",
				value: function(e) {
					var t = this,
					n = arguments.length <= 1 || void 0 === arguments[1] ? !1 : arguments[1],
					o = this._getRoute(e);
					if (!o) throw new Error("url " + e + " was not found");
					return !

					function() {
						if(o.removeParent != undefined && !o.removeParent){
							var r = "function" == typeof o.render ? o.render(o.params) : "",
								i = t._$container.hasChildNodes();
							i && !
							function() {
								var e = t._$container.childNodes[0];
								e.classList.add("weui-parent");
							} ();
							var s = document.createElement("div");
							o.className && s.classList.add(o.className,"weui-child"),
							s.innerHTML = r,
							t._$container.appendChild(s),
							!n && t._options.enter && i && s.classList.add(t._options.enter),
							t._options.enterTimeout > 0 ? setTimeout(function() {
								s.classList.remove(t._options.enter)
							},
							t._options.enterTimeout) : s.classList.remove(t._options.enter),
							location.hash = "#" + e;
							try {
								n ? t._index--:t._index++,
								history.replaceState && history.replaceState({
									_index: t._index
								},
								"", location.href)
							} catch(a) {}
							"function" == typeof o.after ? o.after() : "";
							"function" != typeof o.bind || o.__isBind || (o.bind.call(s), o.__isBind = !0);
						}else{
							var r = "function" == typeof o.render ? o.render(o.params) : "",
								i = t._$container.hasChildNodes();
							i && !
							function() {
								var e = t._$container.childNodes[0];
								//if(t._$container.children.length > 0){
									if(e.classList.contains("weui-parent")){
										e = t._$container.childNodes[1];
								//	}
								}
								n && e.classList.add(t._options.leave),
								t._options.leaveTimeout > 0 ? setTimeout(function() {
									e.parentNode.removeChild(e)
								},
								t._options.leaveTimeout) : e.parentNode.removeChild(e)
							} ();
							
							if(t._$container.childNodes[0] != undefined && t._$container.childNodes[0].classList.contains(o.className)){
								t._$container.childNodes[0].classList.remove("weui-parent");
								
							}else{
								var s = document.createElement("div");
								o.className && s.classList.add(o.className),
								s.innerHTML = r,
								t._$container.appendChild(s),
								!n && t._options.enter && i && s.classList.add(t._options.enter),
								t._options.enterTimeout > 0 ? setTimeout(function() {
									s.classList.remove(t._options.enter)
								},
								t._options.enterTimeout) : s.classList.remove(t._options.enter),
								location.hash = "#" + e;
								try {
									n ? t._index--:t._index++,
									history.replaceState && history.replaceState({
										_index: t._index
									},
									"", location.href)
								} catch(a) {}
								"function" == typeof o.after ? o.after() : "";
								"function" != typeof o.bind || o.__isBind || (o.bind.call(s), o.__isBind = !0);
							}	
						}
					} (),
					this
				}
			},
			{
				key: "_getRoute",
				value: function(e) {
					for (var t = 0,
					n = this._routes.length; n > t; t++) {
						var o = this._routes[t],
						r = a.getRegExp(o.url),
						i = a.getParams(o.url),
						s = r.exec(e);
						if (s) {
							o.params = {};
							for (var u = 0,
							c = i.length; c > u; u++) {
								var l = i[u];
								o.params[l] = s[u + 1]
							}
							return o
						}
					}
					return null
				}
			}]),
			e
		} ();
		t["default"] = u,
		e.exports = t["default"]
	},
	function(e, t) {
		"use strict";
		function n(e, t) {
			for (var n in t) e[n] = t[n];
			return e
		}
		function o(e) {
			return - 1 !== e.indexOf("#") ? e.substring(e.indexOf("#") + 1) : "/"
		}
		function r() {}
		function i(e) {
			var t = /\((.*?)\)/g,
			n = /(\(\?)?:\w+/g,
			o = /\*\w+/g,
			r = /[\-{}\[\]+?.,\\\^$|#\s]/g;
			return e = e.replace(r, "\\$&").replace(t, "(?:$1)?").replace(n,
			function(e, t) {
				return t ? e: "([^/?]+)"
			}).replace(o, "([^?]*?)"),
			new RegExp("^" + e + "(?:\\?([\\s\\S]*))?$")
		}
		function s(e) {
			for (var t = /:(\w+)/g,
			n = [], o = void 0; null !== (o = t.exec(e));) n.push(o[1]);
			return n
		}
		Object.defineProperty(t, "__esModule", {
			value: !0
		}),
		t.extend = n,
		t.getHash = o,
		t.noop = r,
		t.getRegExp = i,
		t.getParams = s
	}])
});