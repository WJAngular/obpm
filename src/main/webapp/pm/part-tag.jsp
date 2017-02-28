<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>

		<div class="pm-page pm-page-tag">
			<div class="cell-content-part project-setting-tag">
				<div class="title">
					<h3>标签</h3>
					<a class="cell-btn-blue J_AddTag" href="javascript:void(0);" data-target="#W_AddTag"><span>+</span>新建标签</a>
				</div>
				<div class="content">
					<ul class="cell-box-list-tag J_BoxList"></ul>
				</div>
			</div>
			<script id="tmplTagItem" type="text/x-jquery-tmpl">
				<li class="cell-box" data-id="${id}" data-name="${name}">
					<div class="cell-box-container">
						<div class="cell-box-content">
							<h4 class="cell-box-title"><span class="icon-tag"></span>${name}</h4>
							<!--<div class="cell-box-dot"></div>-->
							<div class="cell-box-oper" style="display: block;">
								<a class="oper-indi J_OperIndi" href="javascript:void(0);"></a>
								<div class="oper-content-container">
									<p class="arrow-container"><span class="indi-arrow"></span>
									</p>
									<p><a class="oper-item last J_DeleteTag" href="javascript:void(0);" data-id="${id}" data-name="${name}">删除标签</a>
									</p>
								</div>
							</div>
						</div>
					</div>
				</li>
			</script>
			<!-- 弹窗---新建标签 -->
			<div id="W_AddTag" class="popup">
				<div class="popup-title clearfix"><span class="pull-left">新建标签</span><a href="javascript:void(0);" class="pull-right close-popup">X</a>
				</div>
				<div class="popup-co">
					<label for="">标签名称</label>
					<input type="text" placeholder="输入标签名称" />
					<div class="btn-wrap">
						<button class="btn btn-cancel">取消</button>
						<button id="B_AddTag" class="btn btn-primary">创建</button>
					</div>
				</div>
			</div>
			<!-- 弹窗---编辑标签 -->
			<div id="W_EditTag" class="popup">
				<div class="popup-title clearfix"><span class="pull-left">编辑标签</span><a href="javascript:void(0);" class="pull-right close-popup">X</a>
				</div>
				<div class="popup-co">
					<label for="">标签名称</label>
					<input type="text" placeholder="输入标签名称" />
					<div class="btn-wrap">
						<button class="btn btn-cancel">取消</button>
						<button id="B_EditTag" class="btn btn-primary">保存</button>
					</div>
				</div>
			</div>
			<!-- 弹窗---删除标签 -->
			<div id="W_DeleteTag" class="popup">
				<div class="popup-title clearfix"><span class="pull-left">删除标签</span><a href="javascript:void(0);" class="pull-right close-popup">X</a>
				</div>
				<div class="popup-co">
					<div class="warn-tip"><i class="icon-warn"></i>标签删除后不可恢复，您确定要删除标签“<span id="tagName" style="word-break: break-all;"></span>”？</div>
					<div class="btn-wrap">
						<button class="btn btn-cancel">取消</button>
						<button id="B_DeleteTag" class="btn btn-warning">删除</button>
					</div>
				</div>
			</div>
		</div>
