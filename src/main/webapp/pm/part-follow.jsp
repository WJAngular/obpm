<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="pm-page pm-page-follow">
	<div class="cell-content-part project-setting-project">
		<div class="title">
			<h3>关注</h3>
		</div>
		<div class="pm-task-detail-outer"></div>
		<div class="content">
			<table class="pm-task-table">
				<thead>
					<tr class="head">
						<td >任务名称</td>
						<td >优先级</td>
						<td >执行人</td>
						<td >开始日期</td>
						<td >完成日期</td>
						<td >状态</td>
						<!-- <td >操作</td> -->
					</tr>
				</thead>
				<tbody class="tbody" id="pm-task-table-body"></tbody>
			</table>
		</div>
	</div>
</div>
    
<script id="tmplTaskTableListItem" type="text/x-jquery-tmpl">
		<tr class="row" data-id="${id}">
			<td >${name}</td>
			<td >${$item.getLevelText()}</td>
			<td >${executor}</td>
			<td >${startDate}</td>
			<td >${endDate}</td>
			<td >{%if status ==1%}完成 {%else%}未完成{%/if%}</td>
			<!--<td >
				<span class="pm-task-modify-btn pm-task-set-btn" id="pm-task-unfollow-btn" data-id="${id}">
					<button>取消关注</button>
				</span>
			</td>-->
		</tr>
</script>    