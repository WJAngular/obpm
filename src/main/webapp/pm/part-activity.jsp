<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="pm-page pm-page-activity">

	<div class="cell-content-part project-setting-project">
		<div class="title">
			<h3>动态</h3>
		</div>
		<div class="pm-activity-content">
			<article>
				<h3>2013</h3>
				<section>
					<span class="point-time point-yellow"></span>
					<time datetime="2013-03">
						<span>March</span>
						<span>2013</span>
					</time>
					<aside>
						<p class="things">The FAW</p>
						<p class="brief"><span class="text-yellow">Award</span> (Miami. FL)</p>
					</aside>
				</section>
				<section>
					<span class="point-time point-red"></span>
					<time datetime="2013-03">
						<span>March</span>
						<span>2013</span>
					</time>
					<aside>
						<p class="things">You reached 500 followers on Twitter</p>
						<p class="brief"><span class="text-red">Social Appearance</span></p>
					</aside>
				</section>
			</article>
			
			<article>
				<h3>2012</h3>
				<section>
					<span class="point-time point-green"></span>
					<time datetime="2013-03">
						<span>December</span>
						<span>2012</span>
					</time>
					<aside>
						<p class="things">Visitor at Maketing Live 2012</p>
						<p class="brief"><span class="text-green">Conference</span></p>
					</aside>
				</section>
				<section>
					<span class="point-time point-green"></span>
					<time datetime="2012-11">
						<span>November</span>
						<span>2012</span>
					</time>
					<aside>
						<p class="things">Visitor at Daily web 2012</p>
						<p class="brief"><span class="text-green">Conference</span></p>
					</aside>
				</section>
				<section>
					<span class="point-time point-red"></span>
					<time datetime="2012-09">
						<span>September</span>
						<span>2012</span>
					</time>
					<aside>
						<p class="things">You reached 500 followers on Dribbble</p>
						<p class="brief"><span class="text-red">Social Appearance</span></p>
					</aside>
				</section>
				<section>
					<span class="point-time point-blue"></span>
					<time datetime="2012-08">
						<span>August</span>
						<span>2012</span>
					</time>
					<aside>
						<p class="things">New job position as Senior Designer at Fantasy Interactive</p>
						<p class="brief"><span class="text-blue">Working Experience</span></p>
					</aside>
				</section>
				<section>
					<span class="point-time point-red"></span>
					<time datetime="2012-03">
						<span>March</span>
						<span>2012</span>
					</time>
					<aside>
						<p class="things">Speaker at ASMO conference</p>
						<p class="brief"><span class="text-red">Conference</span></p>
					</aside>
				</section>
				<section>
					<span class="point-time point-blue"></span>
					<time datetime="2012-02">
						<span>February</span>
						<span>2012</span>
					</time>
					<aside>
						<p class="things">You added new skills to job position Junior Designer at Fantasy Interactive</p>
						<p class="brief"><span class="text-blue">Working Experience</span></p>
					</aside>
				</section>
			</article>
		</div>
	</div>
</div>

<script id="tmplActivityItem" type="text/x-jquery-tmpl">
	<section>
		<span class="point-time point-green"></span>
		<time datetime="${operationDate}">
			<span>${userName}</span>
			<span>${operationTime}</span>
		</time>
		<aside>
			<p class="things"><span style="color: #000000;font-weight: bold;">${operationType}</span></p>
			<p class="brief">${summary}</p>
		</aside>
	</section>
</script>
<script id="tmplActivityPart" type="text/x-jquery-tmpl">
	<article id="data-part-${operationDate}">
		<h3>${operationDate}</h3>
	</article>
</script>