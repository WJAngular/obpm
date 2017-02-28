package com
{
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.text.TextField;
	import flash.ui.Mouse;
	import flash.ui.MouseCursor;
	
	import mx.core.UIComponent;
 
 
	/**
	 * 当修改了boxWidth,boxHeight时触发
	 */
	[Event(name = "resize", type = "flash.events.Event")]
 
	/**
	 * 当位置改变后触发,这个没有现成的常量,请直接用 "box_move"
	 * @example	addEventListener("box_move", moveHandler);
	 */
	[Event(name = "box_move", type = "flash.events.Event")]
 
	/**
	 * 这是一个有4个或8个缩放点的缩放盒子, 类似变形工具一样的效果<br />
	 * 这个可以拖动<br />
	 * 当拖动缩放点改变,或用resize方法更改尺寸后,触发resize事件<br />
	 * 当改变位置后,触发move事件<br />
	 * 由于用到flashplayer10的鼠标样式,请用用fp10测试<br />
	 * 
	 * 在Flex里使用<br />
	 * 如果想在flex里应用,请将基类修改为 UIComponent <br />
	 * 注意这里宽高使用的boxWidth,boxHeight, 请不要是用width,height<br/>
	 * 
	 */
	public class ScaleBox extends UIComponent
	{
		private var _dragEnable:Boolean = true;// 是否可拖动
		private var _scaleEnable:Boolean = true;//是否可缩放
		private var _is8dot:Boolean = true;	// 是否8个控制点, false为4个, 默认8个
		private var _boxWidth:Number;
		private var _boxHeight:Number;
 
		private var changeW:int;			// 当前控制点 -1:左 , 0:中间  1:右边
		private var changeH:int;			// 当前控制点 -1:上 , 0:中间  1:下边
 
		private var isMove:Boolean = false;	// 是否拖动
		private var lockX:Number;
		private var lockY:Number;
 
		private var bottomRightX:Number;	// 右下角x坐标(父级坐标空间)
		private var bottomRightY:Number;	// 右下角y坐标(父级坐标空间)
 
		private var dotList:Array = [];		// 控制点数组
 
		// 作为scaleBox的内容用的, 测试发现 用graphics 填充,不能很好的触发 over out事件
		private var hitSprite:Sprite;	
		private var txt:TextField;
 
		public function ScaleBox(boxWidth:Number = 100, boxHeight:Number = 100, is8dot:Boolean = true) 
		{
			super();
			initUI();
			// 用graphics画线,不能很好的响应 over out 事件, 则 设置本身不接受鼠标事件
			mouseEnabled = false; 
			addMouseCursorHandler();
			addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			this.is8dot = is8dot;
			resize(boxWidth, boxHeight);
		}
 
		/**
		 * 重置 box 尺寸 
		 * @param	boxWidth	<b>	Number	</b> 宽度 >= 1
		 * @param	boxHeight	<b>	Number	</b> 高度 >= 1
		 * @eventType	flash.events.Event
		 */
		public function resize(boxWidth:Number, boxHeight:Number):void
		{
			if (isNaN(boxWidth) || isNaN(boxHeight)) return;
			if (boxWidth < 1 || boxHeight < 1) return;
 
			_boxWidth  = boxWidth;
			_boxHeight = boxHeight;
			bottomRightX = x + boxWidth;
			bottomRightY = y + boxHeight;
 
			txt.text = boxWidth + " * " + boxHeight;
			var halfW:Number = boxWidth  * .5;
			var halfH:Number = boxHeight * .5;
 
			for (var i:int = 0; i < 8; i++)
			{
				var dot:Dot = dotList[i];
				dot.x = halfW * dot.lx
				dot.y = halfH * dot.ly
			}
 
			this.graphics.clear();
			this.graphics.lineStyle(1);
			this.graphics.drawRect(0, 0, boxWidth, boxHeight);
			this.graphics.endFill();
			hitSprite.width = boxWidth;
			hitSprite.height = boxHeight;
			dispatchEvent(new Event(Event.RESIZE));
		}
 
		/**
		 * 缩放盒子的宽度
		 * @default 100
		 */
		public function get boxWidth():Number { return _boxWidth; }
		/**
		 * 缩放盒子的宽度
		 * @default 100
		 */
		public function set boxWidth(value:Number):void 
		{
			resize(value, _boxHeight);
		}
 
		/**
		 * 缩放盒子的高度
		 * @default 100
		 */
		public function get boxHeight():Number { return _boxHeight; }
		/**
		 * 缩放盒子的高度
		 * @default 100
		 */
		public function set boxHeight(value:Number):void 
		{
			resize(_boxWidth, value)
		}
 
		override public function set x(value:Number):void
		{
			if (isNaN(value)) return;
 
			super.x = value;
			bottomRightX = value + _boxWidth;
			dispatchEvent(new Event("box_move"));
		}
 
		override public function set y(value:Number):void
		{
			if (isNaN(value)) return;
 
			super.y = value;
			bottomRightY = value + _boxHeight;
			dispatchEvent(new Event("box_move"));
		}
 
		/**
		 * 是否8个缩放点, 4个或8个,
		 * @default	true  
		 */
		public function get is8dot():Boolean { return _is8dot; }
		/**
		 * 是否8个缩放点, 4个或8个,
		 * @default	true  
		 */
		public function set is8dot(value:Boolean):void 
		{
			if (_scaleEnable && _is8dot != value)
			{
				_is8dot = value;
				dotList[1].visible = value;
				dotList[3].visible = value;
				dotList[5].visible = value;
				dotList[7].visible = value;
			}
		}
 
		/**
		 * 是否可用鼠标缩放大小
		 * @default true
		 */
		public function get scaleEnable():Boolean { return _scaleEnable; }
		/**
		 * 是否可用鼠标缩放大小
		 * @default true
		 */
		public function set scaleEnable(value:Boolean):void 
		{
			if (_scaleEnable != value)
			{
				_scaleEnable = value;
				for each(var dot:Object in dotList)
				{
					dot.visible = value;
				}
				if (value && !is8dot)
				{
					_is8dot = !value;
					is8dot = value;
				}
			}
		}
 
		/**
		 * 是否可拖动
		 * @default true
		 */
		public function get dragEnable():Boolean { return _dragEnable; }
		/**
		 * 是否可拖动
		 * @default true
		 */
		public function set dragEnable(value:Boolean):void 
		{
			if (_dragEnable != value)
			{
				_dragEnable = value;
 
				if (value) addChildAt(hitSprite, 0);
				else removeChild(hitSprite);
			}
		}
 
		private function mouseOverHandler(e:MouseEvent):void 
		{
			if (e.target is Dot)
			{
				Mouse.cursor = MouseCursor.BUTTON;
			}else
			{
				Mouse.cursor = MouseCursor.HAND;
			}
		}
 
		private function rollOutHandler(e:MouseEvent):void 
		{
			Mouse.cursor = MouseCursor.AUTO;
		}
 
		private function mouseDownHandler(e:MouseEvent):void 
		{
			if (e.target is Dot)
			{
				isMove = false;
				var dot:Dot = e.target as Dot;
				changeW = dot.lx -1;
				changeH = dot.ly -1;
			}else
			{
				isMove = true;
				lockX = mouseX;
				lockY = mouseY;
			}
 
			removeMouseCursorHandler();
			stage.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			stage.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
		}
 
		private function mouseUpHandler(e:MouseEvent):void 
		{
			stage.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			stage.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			addMouseCursorHandler();
		}
 
		private function mouseMoveHandler(e:MouseEvent):void 
		{
			// 拖动
			if (isMove)
			{
				x = parent.mouseX - lockX;
				y = parent.mouseY - lockY;
			}
			// 缩放
			else
			{
				var w:Number, h:Number;
				var tx:Number = x;
				var ty:Number = y;
 
				if (changeW < 0) tx = mouseX + tx;
				else if (changeW > 0) bottomRightX = mouseX + tx;
 
				if (changeH < 0) ty = mouseY + ty;
				else if (changeH > 0) bottomRightY = mouseY + ty;
 
				w = bottomRightX - tx;
				h = bottomRightY - ty;
 
				if (0 == w || 0 == h) return;
 
				var tem:Number;
				if (w < 0)
				{
					w = -w;
					changeW = -changeW;
					tem = tx;
					tx = bottomRightX;
					bottomRightX = tem;
				}
				if (h < 0)
				{
					h = -h;
					changeH = -changeH;
					tem = ty;
					ty = bottomRightY;
					bottomRightY = tem;
				}
 
				super.x = tx;
				super.y = ty;
				dispatchEvent(new Event("box_move"));
				resize(w, h);
			}
		}
 
		// 添加鼠标指针处理器
		private function addMouseCursorHandler():void
		{
			addEventListener(MouseEvent.MOUSE_OVER, mouseOverHandler);
			addEventListener(MouseEvent.ROLL_OUT,  rollOutHandler);
		}
		// 删除鼠标指针处理器
		private function removeMouseCursorHandler():void
		{
			removeEventListener(MouseEvent.MOUSE_OVER, mouseOverHandler);
			removeEventListener(MouseEvent.ROLL_OUT,  rollOutHandler);
		}
 
		private function initUI():void
		{
			txt = new TextField();
			txt.wordWrap = false;
			txt.multiline = false;
			txt.width = 100;
			txt.mouseEnabled = false;
			txt.text = " ";
			txt.x = x;
			txt.y = -txt.textHeight;
			addChild(txt);
 
			hitSprite = new Sprite();
			hitSprite.graphics.beginFill(0, 0);
			hitSprite.graphics.drawRect(0, 0, 10, 10);
			hitSprite.graphics.endFill();
			addChild(hitSprite);
 
			for (var i:int = 0; i < 8; i++)
			{
				var dot:Dot = new Dot();
				addChild(dot);
				dotList.push(dot);
 
				// 排列 位置
				dot.lx = i % 3;
				dot.ly = i / 3;
				if (4 == i)
				{
					dot.lx = 2;
					dot.ly = 2;
				}
			}
		}
	}
}
