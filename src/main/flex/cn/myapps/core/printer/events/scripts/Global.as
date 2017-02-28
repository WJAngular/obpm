// ActionScript file
import main.flex.cn.myapps.core.printer.data.Treap;
import main.flex.cn.myapps.core.printer.util.PaperFormat;

import flash.ui.ContextMenu;

import mx.collections.ArrayCollection;

public var paperFormat:PaperFormat = new PaperFormat([595, 842],"A4",[8.27,11.9],[210,297]);
private var getFieldsUrl:String ="../printer/getFields.action";
private var getSubViewsUrl:String ="../printer/getSubViews.action";
private var getTemplateUrl:String ="../printer/edit.action";
private var saveUrl:String ="../printer/save.action";

private var formCount:uint =0;
private var headerCount:uint =0;
private var footerCount:uint =0;

private var canvasTreap:Treap = new Treap();
private var formItemTreap:Treap = new Treap();//存储所有的form元素的组件
private var detailItemTreap:Treap = new Treap();//存储所有的detail元素的组件
private var headerItemTreap:Treap = new Treap();
private var footerItemTreap:Treap = new Treap();


public var bc:Boolean=false;
public var bdc:Boolean=false;
public var pSelectedItem:ItemDraw=null;//选中的物件
public var pSelectedCanvas:MyCanvas=null;//选中的物件
public var pOldSelectedItem:ItemDraw=null;//刚才选中的物件
public var pDragControl:UIComponent=null;//拖动的物件
public var treeNodeStr:String="";//树节点字符串



private var formid:String;
private var activityid:String;
private var printWithFlow:String = "0";
private var controlPressable: Boolean = false; //存储组件的按下状态
private var pTreapControl:Treap = new Treap();//存储所有的控制方块

private var fieldList:XML;//表单字段
private var subViewList:XML;//子视图
[Bindable]
private var popMenuItem:XML = <nodes>
									<node label="清空画板" name="CleanCanvar"/>  
									<node label="页面设置" name="PageProperty"/>
									<node label="生成XML" name="GenerateXML"/>
							</nodes>;

//组件右键菜单
[Bindable]
private var controlMenu: ContextMenu;
private var canvasMenu: ContextMenu;
//画布右键菜单
[Bindable]
private var reportMenu: ContextMenu;
//属性对话框中的数据
[Bindable]
private var properties: ArrayCollection = new ArrayCollection();


