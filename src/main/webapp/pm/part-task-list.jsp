<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="pm-page pm-page-taskTable">
	<div class="cell-content-part project-setting-project">
		<div class="title pro-task">
			<h3></h3>
			<div class="task-pro">
				<button type="button" class="pro-name btn dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				<span></span>
				<i class="fa fa-chevron-down"></i>
				</button>
				
				<ul class="dropdown-menu project-list">
				</ul>
			</div>
			<a class="cell-btn-blue newTaskInPro" href="javascript:void(0);" data-target="#W_ProAddTask">
						<span>+新建任务</span>
					</a> 
			<div class="search-input on-search-active"> 
				<input name="search-task-projectName" class="nav-search-input" type="text" placeholder="请输入关键字，按回车键搜索"> 
			</div>			
		</div>
		<!-- 高级搜索开始 -->
		<div class="search-advance">
			<div class="skin-dwz" style="display:none;">
				<div class="search-advance-li">
					<label>优先级</label>
					<select name="search-task-level">
						<option value=""></option>
						<option value="3">很重要-很紧急</option>
						<option value="2">很重要-不紧急</option>
						<option value="1">不重要-很紧急</option>
						<option value="0">不重要-不紧急</option>
					</select>
				</div>
				<div class="search-advance-li">
					<label>创建人</label>
					<select name="search-task-creater">
						<option></option>
					</select>
				</div>
				<div class="search-advance-li">
					<label>执行人</label>
					<select name="search-task-executor">
						<option></option>
					</select>
				</div>
				<div class="search-advance-li">
					<label>时间范围</label>
					<select name="search-task-endDate">
						<option value=""></option>
						<option value="LastMonth">上月</option>
						<option value="LastWeek">上周</option>
						<option value="LastThreeDay">前三天</option>
						<option value="Today">今天</option>
						<option value="NextThreeDay">后三天</option>
						<option value="ThisWeek">本周</option>
						<option value="NextWeek">下周</option>
						<option value="ThisMonth">本月</option>
						<option value="NextMonth">下月</option>
					</select>
				</div>
				<div class="search-advance-li">
					<label>标签</label>
					<select name="search-task-tags"></select>
				</div>
				<div class="search-advance-li">
					<label>状态</label>
					<select name="search-task-status">
						<option value="-100"></option>
						<option value="0">新建</option>
						<option value="2">处理中</option>
						<option value="3">已解决</option>
						<option value="1">已完成</option>
						<option value="-1">作废</option>
					</select>
				</div>
				<div class="search-advance-li">
					<label>过期</label>
					<select name="search-task-overdue">
						<option value=""></option>
						<option value="OVERDUE">过期</option>
					</select>
				</div>
				<button class="search-submit" style="display:none;">查询</button>
				<button class="search-reset" style="display:none;">重置</button>
			</div>
			<div class="skin-h5">
				<div class="btn-group search-dropdown-level" _name="level">
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					优先级 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a _value="">&nbsp;</a></li>
						<li><a _value="3"><i class="fa fa-circle-o level3"></i> 很重要-很紧急</a></li>
						<li><a _value="2"><i class="fa fa-circle-o level2"></i> 很重要-不紧急</a></li>
						<li><a _value="1"><i class="fa fa-circle-o level1"></i> 不重要-很紧急</a></li>
						<li><a _value="0"><i class="fa fa-circle-o level0"></i> 不重要-不紧急</a></li>
					</ul>
				</div>
				<div class="btn-group search-dropdown-creater" _name="creater">
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					创建人 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a _value="">&nbsp;</a></li>
					</ul>
				</div>
				<div class="btn-group search-dropdown-executor" _name="executor">
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					执行人 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a _value="">&nbsp;</a></li>
					</ul>
				</div>
				<div class="btn-group search-dropdown-endDate" _name="endDate">
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					时间范围 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">					
						<li><a _value="">&nbsp;</a></li>
						<li><a _value="LastMonth">上月</a></li>
						<li><a _value="LastWeek">上周</a></li>
						<li><a _value="LastThreeDay">前三天</a></li>
						<li><a _value="Today">今天</a></li>
						<li><a _value="NextThreeDay">后三天</a></li>
						<li><a _value="ThisWeek">本周</a></li>
						<li><a _value="NextWeek">下周</a></li>
						<li><a _value="ThisMonth">本月</a></li>
						<li><a _value="NextMonth">下月</a></li>
					</ul>
				</div>
				<div class="btn-group search-dropdown-tags" _name="tags">
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					标签 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a _value="">&nbsp;</a></li>
					</ul>
				</div>
				<div class="btn-group search-dropdown-status" _name="status">
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					状态 <span class="caret"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a _value="-100">&nbsp;</a></li>
						<li><a _value="0">新建</a></li>
						<li><a _value="2">处理中</a></li>
						<li><a _value="3">已解决</a></li>
						<li><a _value="1">已完成</a></li>
						<li><a _value="-1">作废</a></li>
						
					</ul>
				</div>
				<div class="btn-group search-dropdown-submit">
					<button class="btn btn-default">查询</button>
				</div>
				<div class="btn-group search-dropdown-reset">
					<button class="btn btn-default">重置</button>
				</div>
				<div class="btn-group search-boot-switch">
					<span class="overdue">过期</span>
					<div style="width:75px;cursor: pointer;">
				    	<label>
							<input type="checkbox" class="ios-switch green" /><div>
				                <div>
				                </div>
				            </div>
				        </label>
			        </div>
			    </div>
			</div>
		</div>
		<!-- 高级搜索结束 -->
		<div class="pm-task-detail-outer"></div>
		<div class="content">
			<div class="pm-task-list-all">
				<table class="pm-task-table" id="pm-follow-task-table" style="table-layout: fixed;">
					<thead>
						<tr class="head">
						    <td _orderName="STATUSORDER" class="pm-list-order" width="100px"><span>状态</span></td>
							<td _orderName="NAME" class="pm-list-order"><span>任务名称</span></td>
							<td _orderName="LEVELS" class="pm-list-order" width="130px"><span>优先级</span></td>
							<!-- <td >执行人</td>
							<td >开始日期</td>
							<td >完成日期</td> -->
							<td _orderName="TAGS" class="pm-list-order" width="100px" style="text-overflow: ellipsis;overflow: hidden;white-space: nowrap;"><span>标签</span></td>
							<td width="100px">进度</td>
							<!-- <td>项目成员</td>
							<td >操作</td> -->
						</tr>
					</thead>
					<tbody class="tbody" id="pm-task-table-body2"></tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 弹窗---新建任务-->
	<div id="W_ProAddTask" class="popup">
		<div class="popup-title clearfix"><span class="pull-left">新建任务</span><a href="javascript:void(0);" class="pull-right close-popup">X</a>
		</div>
		<div class="popup-co">
			<label for="">任务名称</label>
			<input type="text" placeholder="输入任务名称,回车完成输入" />
			<div class="btn-wrap">
				<button class="btn btn-cancel">取消</button>
				<button id="B_ProAddTask" class="btn btn-primary">创建</button>
			</div>
		</div>
	</div>
</div>

<script id="tmplTaskTableListItem2" type="text/x-jquery-tmpl">
		<tr class="row" data-id="${id}">
			<td class="project-status"><span class="label 
				{%if status == 0%}label-status0{%/if%}
				{%if status == 1%}label-status1{%/if%}
				{%if status == 2%}label-status2{%/if%}
				{%if status == 3%}label-status3{%/if%}
				{%if status == -1%}label-status-1{%/if%}">
				{%if status == 0%}新建{%/if%}
				{%if status == 1%}已完成{%/if%}
				{%if status == 2%}处理中{%/if%}
				{%if status == 3%}已解决{%/if%}
				{%if status == -1%}作废{%/if%}</span></td>
			<td class="project-title">
				<div class="project_task_name" style="line-height: 34px;"><a>${name}</a></div>
				<div style="line-height: 20px;margin-bottom: 10px;"><span class="executor">负责人&nbsp;${executor}</span> <span class="startDate">${startDate}</span><span>——</span><span class="endDate">${endDate}</span></div>
				{%if $item.ifOverdue() ==true%}<div class="projecr_task_guoqi"><img src="./images/guoqi.png"/></div>{%/if%}
			</td>
			<td class="project-level">{{html $item.getLevelText()}}</td>
			<!--<td >${name}</td>
			<td >{{html $item.getLevelText()}}</td>
			<td >${executor}</td>
			<td >${startDate}</td>
			<td >${endDate}</td>-->
			<td class="project-tag">{{each tags}}<p>  ${$value.name}</p>{{/each}}</td>
			<td class="project-completion">
				<div class="progress-title">当前进度：{%if status ==1%}100%{%else%}0%{%/if%}</div>
				<div class="progress progress-mini">
                     <div style="width: {%if status ==1%}100%{%else%}0%{%/if%};" class="progress-bar"></div>
                </div>
			</td>
			<!--<td class="project-people">
                <a><img alt="image" class="img-circle" src="images/a1.jpg"></a>
            </td>
			<td >
			<td >${endDate}</td>
			<td >{%if status ==1%}完成 {%else%}未完成{%/if%}</td>
			<!-- <td >
				<span class="pm-task-modify-btn pm-task-set-btn" id="pm-task-unfollow-btn" data-id="${id}" {%if !hasFollow%} style="display:none;" {%/if%}>
				<button>取消关注</button></span>
				<span class="pm-task-modify-btn pm-task-set-btn" id="pm-task-follow-btn" data-id="${id}" {%if hasFollow%} style="display:none;" {%/if%}>
				<button>关注</button></span>
			</td>
			<td class="project-actions">
                <a class="btn btn-white btn-sm" style="display:none;"><i class="fa fa-folder"></i> 查看 </a>
                <a class="btn btn-white btn-sm"><i class="fa fa-pencil"></i> 编辑 </a>-->
            </td>
			</td>
		</tr>
		
</script>  
