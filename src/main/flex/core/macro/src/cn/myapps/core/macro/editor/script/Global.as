// ActionScript file
import cn.myapps.core.macro.editor.components.editor.Editor;
import cn.myapps.core.macro.editor.components.helper.IFrame;

import flash.text.TextFormat;

import mx.containers.Panel;



public var iscriptContent:String ='';
public var iscriptLabel:String ='Undefined';
public var helperLink:String ='';
public var ctrl_down:Boolean = false;
public var s_down:Boolean = false;

public var editor:Editor = null;
public var helper:Panel = null;
public var  iframe:IFrame = null;

public var _textFormat:TextFormat = new TextFormat;


[Bindable] private var controlPanelDate:XML = <nodes>
									<node label="常用" data="common"/>
									<node label="其他" data="undefind"/>
							</nodes>;

[Bindable] private var menuItem:XML = <menu>
										<items label="文件" name="文件">  
											<item label="保存" name="save"/>
											<item label="退出" name="exit"/>
										</items>
										<items label="编辑" name="编辑">  
											<item label="复制" name="copy"/>
											<item label="粘贴" name="paste"/>
										</items>
									  </menu>;
