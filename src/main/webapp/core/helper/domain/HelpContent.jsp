<%@include file="/common/taglibs.jsp"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<% 
	String contextPath = request.getContextPath();
	String helpnodeid = request.getParameter("id");
%>
<html><o:MultiLanguage>
<head>
<title>domain/HelpContent.jsp</title>
<link rel="stylesheet" href="<s:url value='/core/helper/helper.css'/>" type="text/css" />
<script type="">
	var contextPath='<%=contextPath%>';
	var helpnodeid='<%=helpnodeid%>';
</script>
<script src="<s:url value="/script/list.js"/>"></script>
<script src="<s:url value='/script/help.js'/>"></script>

</head>
<body style="margin: 0px;padding: 0px;">
<h2>XXXXXXXXXXXXXXXXX</h2>

<p>The following tips and tricks give some helpful ideas for increasing your
productivity. They are divided into the following sections:</p>
<ul>
<li><a href="#Workbench">Workbench</a></li>
<li><a href="#Editing">Editing</a></li>
<li><a href="#Ant">Ant</a></li>
<li><a href="#Help">Help</a></li>
<li><a href="#CVS">Team - CVS</a></li>
</ul>

<h3><a name="Workbench">Workbench</a></h3>

<table border="1" cellpadding="10" cellspacing="0" width="800">
  <tr>
    <td width="20%" valign="top" align="left"><b>Now, where was I?</b></td>
    <td width="80%" valign="top" align="left">Workbench editors keep a navigation 
      history. If&nbsp; you open a second editor while you're editing, you 
      can press <b>Navigate &gt; Backward</b>  (Alt+Left Arrow, or the <img border="0" src="images/backward_nav.png" alt="Left arrow icon" >
      back arrow 
      on the workbench toolbar) to go back to the last editor. This makes working with several open editors a whole lot easier.</td>
  </tr>
  
  
  <tr> 
    <td width="20%" valign="top" align="left"><b>Ctrl+3 Quick Access</b></td>
    <td width="80%" valign="top" align="left">You can quickly find all manner of contributions with the Ctrl-3 binding including (but not limited to) open editors, available perspectives, views, preferences, wizards, and commands.
    Simply start typing the name of the item you wish to invoke and we will attempt to find something in the Workbench that matches the provided string.
    <p align="center"><img src="images/quickaccess.png" alt="Quick Access dialog" > </p>
    </td>    
  </tr>
  
  <tr> 
    <td width="20%" valign="top" align="left"><b>Ctrl+E Editor List</b></td>
    <td width="80%" valign="top" align="left">You can quickly switch editors using 
      the Ctrl+E keybinding which opens a list of all open editors. The list supports 
      type-ahead to find the editor as well as allows you to close editors using 
      a popup menu or the Delete key. </td>
  </tr>
 
  <tr> 
    <td width="20%" valign="top" align="left"><b>Like to start afresh each session?</b></td>
    <td width="80%" valign="top" align="left">A setting on the 
    
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Editors)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General &gt; Editors</strong></a>

      preference page closes all open editors automatically whenever 
      you exit. This makes start-up cleaner and a bit faster.</td>
  </tr>
    <tr> 
    <td width="20%" valign="top" align="left"><b>Opening editors using drag and 
      drop</b></td>
    <td width="80%" valign="top" align="left">You can open an editor on an item 
      by dragging the item from a view like the Project Explorer or Package Explorer 
      and dropping it over the editor area.</td>
  </tr>
     <tr> 
    <td width="20%" valign="top" align="left"><b>Tiling the editor work area</b></td>
    <td width="80%" valign="top" align="left">You can use drag and drop to modify 
      the layout of your editor work area. Grab an editor tab and drag it to the 
      edge of the editor work area. The arrow dock icons (e.g., <img src="../images/drop_left_source.png" align="texttop" border="0" alt="Left arrow icon">) 
      indicate which way the editor work area will split. 
      <p><img src="images/editor-tiles.png" alt="Tiled editor work area"  border="0"></p></td>
  </tr>
    <tr> 
    <td width="20%" valign="top" align="left"><b>Open editors with a single click</b></td>
    <td width="80%" valign="top" align="left">Use the Open mode setting on the 
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Workbench)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General</strong></a>
      
      preference page to activate single click opening for editors. 
      In single click mode, a single click on a file in the Project Explorer view (and 
      similar views) selects and immediately opens it. </td>
  </tr>
    <tr> 
    <td width="20%" valign="top" align="left"><b>Collapsing all open items</b></td>
    <td width="80%" valign="top" align="left">Use the <b>Collapse All</b> button 
      on the toolbar of the Project Explorer view (and similar views) to collapse all 
      expanded project and folder items. 
      <p align="center"><img src="images/collapse-all.png" alt="Collapse all button on navigator toolbar" > </p> </td></tr>
    <tr> 
    <td valign="top" align="left" width="20%"> <strong>Global find/replace</strong> 
    </td>
    <td valign="top" align="left" width="80%"> Use
    
      <a class="command-link" href='javascript:executeCommand("org.eclipse.search.ui.openFileSearchPage")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Search &gt; File</strong></a>
    
      from the main menu to specify the text that you want to replace and the scope
      in which you want to replace it. Then press <b>Replace...</b>.</td>
  </tr>
  <TR>
		<TD width="20%" valign="top" align="left"><B>Replace from Search view</B>
		</TD>
		<TD width="80%" valign="top" align="left">You can replace the matches in the files by using <B>Replace...</B> or <B>Replace
			Selected...</B> from the context menu in the Search view.</TD>
  </TR>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Linking view to current open 
      editor</b></td>
    <td width="80%" valign="top" align="left"> <p>The resource Project Explorer view 
        (and similar views) is not tightly linked to the currently open editor 
        by default. This means that closing or switching editors does not change 
        the selection in the Project Explorer view. Toggling the <b>Link with Editor</b> 
        button in the Project Explorer view toolbar ties the view to always show the 
        current file being edited. </p>
      <p align="center"><img src="images/link-with-editor.png" alt="Project Explorer linked with editor"  border="0"></p></td>
  </tr>
    <tr> 
    <td width="20%" valign="top" align="left"><b>Manual editor / view 
      synchronization</b></td>
    <td width="80%" valign="top" align="left">The <b>Navigate &gt; Show In</b> 
      command provides a uniform way to navigate from an open editor to a view 
      showing the corresponding file (e.g., in the resource Project Explorer view), or 
      from a file selected in one view to the same file in a different view (e.g., 
      from the resource Project Explorer view to the Packages Explorer view).
      <p>Typing Alt+Shift+W opens a shortcut menu with the available view
      targets.</p>
      <p><img src="images/show-in.png" alt="Shortcut menu for Show in command"  border="0"></p>
    </td>
  </tr>
    <tr> 
    <td width="20%" valign="top" align="left"><b>Quick navigation between views, 
      editors and perspectives</b></td>
    <td width="80%" valign="top" align="left">A look at the <b>Window &gt; Navigation</b> 
      menu reveals a number of ways to quickly navigate between the various views, 
      editors, perspectives, and menus in the workbench. These commands have keyword 
      accelerators such as <b>Ctrl+F6</b> for switching between editors, <b>Ctrl+F7</b> 
      for switching between views, <b>Ctrl+F8</b> for switching between perspectives, 
      and <b>F12</b> for activating the editor. 
      <p><img src="images/keyboard-shortcut.png" alt="Navigation shortcuts menu"  border="0"></p> 
      <p>To directly navigate to a particular view you can define a keyboard shortcut 
        to a view via the <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Keys)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General &gt; Keys</strong></a></p></td>
  </tr>
    <tr> 
    <td width="20%" valign="top" align="left"><b>Pinning editors</b></td>
    <td width="80%" valign="top" align="left">When the <b> Close editors automatically</b> 
      preference is active (found on the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Editors)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General &gt; Editors</strong></a>

      preference page), you can stop an editor from being closed by using the
      <b>Pin Editor</b> button which appears in the workbench toolbar. 
      <p><img src="images/pin-editor.png" alt="Pin editor button"  border="0"></p></td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Reordering editor tabs</b></td>
    <td width="80%" valign="top" align="left">You can rearrange the order of open 
      editors by using drag and drop. Grab the editor tab and drag it to the position 
      you want the editor to appear. When positioning editors, the stack icon 
      <img src="../images/drop_stack.png"  align="texttop" border="0" alt="Stack icon"> 
      indicates a valid spot to drop.</td>
  </tr>
     <tr> 
    <td width="20%" valign="top" align="left"><b>Minimizing Views and Editors</b>
    </td>
    <td width="80%" valign="top" align="left">Running out of space? Try minimizing 
      your unused views to reclaim screen real-estate. Each view stack contains 
      a minimize icon along side the maximize icon. 
      <p align="center"><img src="images/mini-view.png" alt="Minimized View Stack"  border="0" align="left"></p></td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Maximizing Views and Editors</b></td>
    <td width="80%" valign="top" align="left">You can maximize a view or editor 
      by double-clicking on the view's title bar or the editor's tab. Double-click 
      again to restore it to its usual size.</td>
  </tr>
   <tr>
    <td width="20%" valign="top" align="left"><b>Managing screen real estate 
      with fast views</b></td>
    <td width="80%" valign="top" align="left">Use fast views to free up screen 
      real estate while keeping views easily accessible. Clicking on the icon 
      for a fast view temporarily reveals it over top of the other views. The 
      fast view retracts as soon you click outside of it. The <b>Fast View</b> 
      command in the view's system menu toggles whether it is a fast view. You 
      can also create a fast view by dragging a view onto the Fast View Bar 
      in the bottom left hand corner.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Fast Views and the Perspective 
      Bar</b>
     </td>
    <td width="80%" valign="top" align="left">The fast view and perspective bars 
      may be docked in different locations, independent of one 
      another. 
      <p>By default the Perspective Bar is located in the upper right hand corner 
        of the screen. It may also be docked on the top left, under the main toolbar 
        or to the far left. It may be moved via the perspective bar context menu 
        or via the
        
        <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Views)")'>
        <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
        <strong>General &gt; Appearance</strong></a>
 
        preference page. </p>
      <p align="left"><img src="images/pb-context-menu.png" alt="Perspective Bar Context Menu"  border="0"></p>
      <p>By default the Fast View Bar is located in the bottom left hand corner 
        of the screen. Like the Perspective Bar, it may be docked elsewhere. This 
        may be done by dragging the area to either the left or right side of the 
        screen (or back to the bottom if it is already in one of these positions).</p> 
      <p align="left"><img src="images/fastview-drag.png" alt="Fast View Area"  border="0"></p></td>
  </tr>
     <tr> 
    <td width="20%" valign="top" align="left"><b>Detached Views</b>
    </td>
    <td width="80%" valign="top" align="left"><p>It's possible to detach a view
          so that it can be placed wherever desired, including over another Eclipse 
          window. </p>
        <p>Right-click on the view to be moved and select &quot;Detached&quot; from the menu.
    (Alternatively, drag the view by its tab to detach the view from its position in the perspective.)</p>
        <p><img src="images/detachview1.png" alt="Screenshot of the context menu's 'Detach' option"></p>
        <p>Then, place the view where you choose. You can also drag and drop other views into the same 
          window.</p> 
        <p><img src="images/detachview2.png" alt="Screenshot of the floating view"> </p>
       <p>To return the view to its position, use the context menu's <b>Restore</b> function.
    (Alternatively, drag the view by its tab.)</p></td>
  </tr>
    <tr> 
    <td width="20%" valign="top" align="left"><b>Restoring a perspective's layout</b></td>
    <td width="80%" valign="top" align="left">Rearranging and closing the views 
      in a perspective can sometimes render it unrecognizable and hard to work 
      with. To return it to a familiar state, use
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.resetPerspective")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Window &gt; Reset Perspective</strong></a>.
    </td>
  </tr>
    <tr> 
    <td width="20%" valign="top" align="left"><b>User customizable<br>
      key bindings</b></td>
    <td width="80%" valign="top" align="left">If you find yourself repeatedly 
      doing some command, you might be able to streamline things by assigning 
      a key sequence to trigger that command. Assigning new key bindings, and 
      viewing existing bindings, is done from the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Keys)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General &gt; Keys</strong></a>
      
      preference page. 
      <p><img src="images/key-bindings.png" alt="Key bindings preference dialog"></p></td>
  </tr>
   <tr> 
      <td width="20%" valign="top" align="left"><b>View all 
          keyboard shortcuts</b></td>
      <td width="80%" valign="top" align="left">While working with your favorite editors and 
        views in Eclipse, just press Ctrl+Shift+L to see a full list of the currently 
        available key bindings. This is a great way to learn what is available 
        in the UI and to speed up your productivity by learning more key bindings. 
        This information is also available in the improved
        
        <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Keys)")'>
        <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
        <strong>General &gt; Keys</strong></a>
        
        preference page.
        <p><img border="0" src="images/keycompletions.png" alt="Screenshot of keybindings" ></p></td>
    </tr>
      <tr> 
    <td width="20%" valign="top" align="left"><b>Key Binding Assistance</b></td>
    <td width="80%" valign="top" align="left"><p>Eclipse supports key bindings 
        that contain more than one key stroke. Examples of such key bindings are 
        "Ctrl+X S" ("Save" in the Emacs key configuration) or "Alt+Shift+Q Y" 
        ("Open Synchronize View" in the Default key configuration). It is hard 
        to learn these keys, and it can also be hard to remember them if you don't 
        use them very often. If you initiate such a key sequence and wait a little pop-up showing 
        you the possible completions will appear.</p>
      <p align="left"><img src="images/key-assist.png" alt="Key Assist"  border="0"></p>
      <p>In the preferences, under
      
        <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Keys)")'>
        <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
        <strong>General &gt; Keys</strong></a>,
      
        there is an 
        &quot;Advanced&quot; tab. Go to this tab, and check &quot;Help Me With Multi-Stroke Keyboard 
        Shortcuts&quot;.</p></td>
  </tr>
   <tr> 
    <td width="20%" valign="top" align="left"><b>Customizing toolbar and menu 
      bar</b></td>
    <td width="80%" valign="top" align="left">You can customize which items appear 
      on the main toolbar and menu bar using the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.customizePerspective")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Window &gt; Customize Perspective</strong></a>
      
      command. 
      <p><img src="images/perspective-other.png" alt="Customizing toolbar and menu bar"  border="0"></p></td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Restoring deleted resources</b></td>
    <td width="80%" valign="top" align="left">Select a container resource and 
      use <b>Restore from Local History</b> to restore deleted files. You can 
      restore more than one file at one time. 
      <p align="center"><img src="images/restore-local.png"  border="0" alt="Restore from local history dialog" align="left"></p></td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Faster workspace navigation</b></td>
    <td width="80%" valign="top" align="left">
    
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.navigate.openResource")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Navigate &gt; Open Resource</strong></a>

      (Ctrl+Shift+R) brings up a dialog that allows you to quickly locate and 
      open an editor on any file in the workspace. In the same vein, <b>Navigate 
      &gt; Go To &gt; Resource</b> expands and selects the resource in the Project Explorer 
      view itself, if it has focus.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Copying and moving resources</b></td>
    <td width="80%" valign="top" align="left">You can drag and drop files and 
      folders within the Project Explorer view to move them around. Hold down the Ctrl 
      key to make copies.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Importing files</b></td>
    <td width="80%" valign="top" align="left">You can quickly import files and 
      folders into your workspace by dragging them from the file system (e.g., 
      from a Windows Explorer window) and dropping them into the Project Explorer view. 
      The files and folder are always copied into the project; the originals are 
      not affected. Copy and paste also work.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Exporting files</b></td>
    <td width="80%" valign="top" align="left">Dragging files and folder from the 
      Project Explorer view to the file system (e.g., to a Windows Explorer window) exports 
      the files and folders. The files and folder are always copied; workspace 
      resources are not affected. Copy and paste also work.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Workspace project management</b></td>
    <td width="80%" valign="top" align="left">Use the <b>Project &gt; Close Project</b> 
      command to manage projects within your workspace. When a project is closed, 
      its resources are temporarily &quot;offline&quot; and no longer appear in 
      the Workbench (they are still sitting in the local file system). Closed 
      projects require less memory. Also, since they are not examined during builds, 
      closing a project can improve build times. </td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Describing your configuration</b></td>
    <td width="80%" valign="top" align="left">When reporting a problem, it's often 
      important to be able to capture details about your particular setup. The 
      <b> Configuration Details</b> button on the

      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.help.aboutAction")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Help &gt; About <i>Product</i></strong></a>
      
      dialog opens a file containing various pieces of information about your 
      setup, including plug-in versions, preference settings, and the contents 
      of the internal log file. You can save this, and attach the file to your 
      problem report.</td>
  </tr>
 <tr> 
    <td width="20%" valign="top" align="left"><b>Deleting completed tasks</b></td>
    <td width="80%" valign="top" align="left">Use the <b>Delete Completed Tasks</b> 
      command in the Task view context menu to remove all completed tasks from 
      the Tasks view. This is more convenient than individually selecting and 
      deleting completed tasks.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Viewing resource properties</b></td>
    <td width="80%" valign="top" align="left">Use the Properties view

      (<a class="command-link" href='javascript:executeCommand("org.eclipse.ui.views.showView(org.eclipse.ui.views.showView.viewId=org.eclipse.ui.views.PropertySheet)")'><img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Window &gt; Show View &gt; Properties</strong></a>)
    
      when viewing the properties for many 
      resources. Using this view is faster than opening the Properties dialog 
      for each resource. 
      <p><img src="images/props-view.png" alt="Resource properties dialog"  border="0"></p></td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Quickly find a resource</b></td>
    <td width="80%" valign="top" align="left">Use the <b>Navigate &gt; Go To &gt; 
      Resource</b> command to quickly find a resource. If the <b>Go To &gt; Resource</b> 
      command does not appear in your perspective, you can add it by selecting 
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.customizePerspective")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Window &gt; Customize Perspective</strong></a>,
      
      then <b>Other &gt; Resource Navigation</b>.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Extra resource information</b></td>
    <td width="80%" valign="top" align="left">Label decorations are a general 
      mechanism for showing extra information about a resource. Use the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Decorators)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General &gt; Appearance &gt; Label Decorations</strong></a>
      
      preference page to select which of the available 
      kinds of decorations you want to see.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Filtering resources</b></td>
    <td width="80%" valign="top" align="left">The Project Explorer and Tasks views both 
      support filtering of their items. You control which items are visible by 
      applying filters or working sets. The <b>Filters</b> commands are found 
      on the view menu. The working set is selected using the <b>Select Working 
      Set</b> command in the Project Explorer view menu. In the Tasks view, a working 
      set can be selected from within the <b>Filters</b> dialog.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Quick fix in Tasks view</b></td>
    <td width="80%" valign="top" align="left">You can use the <b>Quick Fix</b> 
      command in the Tasks view to suggest an automatic fix for the selected item. 
      The <b>Quick Fix</b> command is only enabled when there is a suggested fix.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Creating path variables</b></td>
    <td width="80%" valign="top" align="left">When creating a linked folder or 
      file, you can specify the target location relative to a path variable. By 
      using path variables, you can share projects containing linked resources 
      without requiring team members to have exactly the same path in the file 
      system. You can define a path variable at the time you create a linked resource, 
      or via the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.LinkedResources)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General &gt; Workspace &gt; Linked Resources</strong></a>
      
      preference page. 
      <p align="center"><img src="images/path-vars.png" alt="Path variables dialog"></p></td>
  </tr>
  <tr> 
    <td valign="top" align="left" width="20%"> <strong>Comparing zip archives 
      with each other or with a folder</strong> </td>
    <td valign="top" align="left" width="80%"> Select two zip archives or one 
      archive and a folder in the resource Project Explorer view and choose <strong>Compare 
      With &gt; Each Other</strong> from the view's popup menu. Any differences 
      between the two inputs are opened in a Compare editor. The top pane shows 
      all the archive entries that differ. Double clicking on an item performs 
      a content compare in the bottom pane. 
      <p> This works in any context where a file comparison is involved. So if 
        a CVS Synchronize operation lists an archive in the resource tree, you 
        can double click on it in order to drill down into changes within the 
        archive.</p></td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Switch workspace</b>
    </td>
    <td width="80%" valign="top" align="left"><p>Instead of shutting down eclipse and restarting 
    	with a different workspace you can instead use
    	
    	<a class="command-link" href='javascript:executeCommand("org.eclipse.ui.file.openWorkspace")'>
        <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
        <strong>File &gt; Switch Workspace</strong></a>.  From here you can either open previous workspaces 
        directly from the menu or you can open the workspace chooser dialog to choose a new one.
    	
    	</p>
    	
      <p>This trick is also useful when you change certain preferences that require 
        a restart to take effect (such as the
        
        <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Views)")'>
        <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
        <strong>General &gt; Appearance</strong></a>
        
        preferences). To restart quickly simply switch workspaces 
        to your current workspace.</p>
    </td>
  </tr>
  <tr> 
    <td valign="top" align="left"><strong>Always Run in Background</strong>
    </td>
    <td valign="top" align="left"><p>Many operations can be optionally 
        run in the background so that you can continue working while they complete.</p>
      <p><img src="../images/res_build.png" alt="Progress dialog with Run in background button"></p>
      <p>In the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Workbench)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General</strong></a>
      
      preference page you can choose to always 
        run in background so that you never get the initial dialog for these operations.</p>
      <p><img src="images/backgroundpref.png" alt="Always run in background preference" ></p></td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Disabling Unused Capabilities</b></td>
    <td width="80%" valign="top" align="left"><p>If there are parts of the Eclipse 
        Platform that you never use (for instance, you don't use CVS repositories 
        or you don't develop Plug-ins) it's possible that you can disable them 
        from the UI entirely. Segments of the Workbench that may be filtered can 
        be found in the
        
        <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.sdk.capabilities)")'>
        <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
        <strong>General &gt; Capabilities</strong></a>
        
        preference page. By disabling capabilities you are able to hide views,
        perspectives, preference pages and other assorted contributions.</p>
	    
      <p align="left"><img src="../images/cap-pref.png" alt="Capabilities Preference Page"></p>
    </td>
  </tr>  
  </table>

<h3><a name="Editing">Editing</a></h3>
<table border="1" cellpadding="10" cellspacing="0" width="800">
  <tr>
	<td valign="top" align="left" width="20%">
		<b>Finding a string incrementally</b></td>
    <td valign="top" align="left" width="80%">
        Use <b>Edit &gt; Incremental Find Next</b>  (Ctrl+J) or <b>Edit &gt; Incremental Find Previous</b>
        (Ctrl+Shift+J)
        to enter the incremental find mode, and start typing the string to match.
        Matches are found incrementally as you type.
        The search string is shown in the status line. Press Ctrl+J or
        Ctrl+Shift+J to go to the next or previous match. Press Enter or Esc to
        exit incremental find mode.
	</td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Go to last edit location</b></td>
    <td width="80%" valign="top" align="left"><b>Navigate &gt; Go
      to Last Edit Location</b> (Ctrl+Q) takes you back to
      the place where you last made a change. A corresponding button marked <img border="0" src="images/last_edit_pos.png" alt="Go to last edit position icon" >
      is shown in the toolbar. If this toolbar button does not appear in your perspective, you can 
    add it by selecting
    
    <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.customizePerspective")'>
    <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
    <strong>Window &gt; Customize Perspective</strong></a>,
    
    then <b>Other &gt; Editor Navigation</b>.</td>
  </tr>
	<TR>
		<TD width="20%" valign="top" align="left">
      <B>Shortcuts for manipulating lines</B>
      </TD><TD width="80%" valign="top">
      
			All text editors based on the
			Eclipse editor framework support editing functions, including
			moving lines up or down (Alt+Arrow Up and Alt+Arrow Down), copying lines (Ctrl+Alt+Arrow Up and Ctrl+Alt+Arrow Down), inserting
			a new line above or below the current line (Ctrl+Shift+Enter and
			Shift+Enter), and converting to lowercase or uppercase (Ctrl+Shift+Y
			and Ctrl+Shift+X).
			</TD>
		
	</TR>
		<TR>
			<TD width="20%" valign="top" align="left"><B>Quick Diff: seeing what has changed as you edit</B>
			</TD>
			
    <TD width="80%" valign="top"> Quick Diff provides color-coded change indication 
      while you are typing. It can be turned on for text editors using either 
      the ruler context menu, Ctrl+Shift+Q or for all new editors on the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.editors.preferencePages.QuickDiff)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General &gt; Editors &gt; Text Editors &gt; Quick Diff</strong></a>
      
      preference page. The 
      colors show additions, deletions, and changes to the editor buffer as compared 
      to a reference, for example, the contents of the file on disk or its latest 
      CVS revision. 
      <P><img src="images/quickdiff-hover.png"  alt="Quick Diff"></P>
			<P>When the mouse cursor is placed over a change in the vertical
			ruler, a hover displays the original content, which can be restored using the ruler's context
			menu. The context menu also allows you to enable/disable Quick Diff.</P></TD>
		</TR>
		<TR>
			<TD width="20%" valign="top" align="left"><b>Customizing the presentation of 
      annotations</b></TD>
			
    <TD width="80%" valign="top"> You can customize the presentation of annotations 
      in editors on the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.editors.preferencePages.Annotations)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>General &gt; Editors &gt; Text Editors &gt; Annotations</strong></a>
      
      preference page: 
      <P><img src="images/annotations-preferences.png" alt="Annotations preference page" title="Annotations Preference Page">
</P>
			</TD>
		</TR>
 
 
  <tr> 
    <td width="20%" valign="top" align="left"><b>Next / previous navigation</b></td>
    <td width="80%" valign="top" align="left">You can use Ctrl+. and Ctrl+, to 
      navigate to the next or previous search match, editor error, or compare 
      difference. These are the shortcut keys for <b>Navigate &gt; Next</b> and 
      <b>Navigate &gt; Previous</b>.</td>
  </tr>
      <tr> 
    <td width="20%" valign="top" align="left"><b>Line delimiter support</b></td>
    <td width="80%" valign="top" align="left"> <p>You can set the line delimiter that 
          is used when creating new text files. You can provide a single setting 
          for the entire workspace, using the
          
          <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.ui.preferencePages.Workspace)")'>
          <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
          <strong>General &gt; Workspace</strong></a>
          
          preferences, or for a given project. In addition, line 
          delimiter conversions can now be applied to projects, folders, and files, 
          not just to the contents of a single editor.</p>
        <p><img src="images/line-delimiter.png" alt="Screenshot of default delimiter dialog"></p></td>
    </tr>
        <tr> 
      <td width="20%" valign="top" align="left"><b>Word completion</b></td>
      <td width="80%" valign="top" align="left"><p>In any text editor you can complete a 
        prefix to a word occurring in all currently open editors or buffers. The 
        default key binding for word completion is Alt+/. (Ctrl+. on the Mac).</p>
    </td>
    </tr>

    <tr> 
      <td width="20%" valign="top" align="left"><b>Open untitled 
          files</b></td>
      <td width="80%" valign="top" align="left"><p>A text editor can be opened without creating 
        a file first: select <strong>File &gt; New &gt; Untitled Text File</strong>.
        </p></td> 
    </tr>
  

</table>

<h3><a name="Ant">Ant</a></h3>
<table border="1" cellpadding="10" cellspacing="0" width="800">
  <tr>
    <td width="20%" valign="top" align="left"><b>Launching from the Context menu</b>
    </td>
    <td width="80%" valign="top" align="left">You can launch an Ant build from 
      the context menu. Select an Ant buildfile and then choose <b>Run &gt; Ant 
      Build</b> from the context menu. To configure options before running the 
      build, use <strong>Run &gt; Ant Build...</strong> which will open the
      launch configuration dialog. A build can also be started from the Ant
      editor outline context menu.</td>
  </tr>
   <tr>
    <td width="20%" valign="top" align="left"><b>Specification of JRE</b>
    </td>
    <td width="80%" valign="top" align="left">You can specify the JRE that an 
      Ant build occurs in using the <b>JRE</b> tab of the launch configuration 
      dialog for an Ant launch configuration. The build can be set to run in a 
      separate JRE (the default setting) or the same JRE as the Eclipse workspace. 
      Note that some Eclipse specific tasks require that the build occurs in the 
      same JRE as Eclipse.</td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Running Ant targets in the Ant view</b></td>
    <td width="80%" valign="top" align="left">You can double click on a target
      in the Ant view to run it (equivalent to selecting the target and choosing
      the <b>Run</b> command from the context menu). </td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Terminating Ant builds</b></td>
    <td width="80%" valign="top" align="left">The <b>Terminate</b> command in the
      console (or Debug view) can be used to terminate an Ant build running in
      the background.</td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Ant output and hyperlinks</b></td>
    <td width="80%" valign="top" align="left">The output from Ant builds is
      written to the <b>Console</b> view in the same hierarchical format seen
      when running Ant from the command line. Ant tasks (for example &quot;[mkdir]&quot;)
      are hyperlinked to the associated Ant buildfile, and javac error reports
      are hyperlinked to the associated Java source file and line number.
      <p>The Console supports hyperlinks for javac and jikes as well as the
        Eclipse Java compiler. All such error reports are hyperlinked to the associated 
        Java source file and line number.</p></td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Ant can find it</b></td>
    <td width="80%" valign="top" align="left"> When the <b>Run &gt; External Tools &gt; Run As &gt; Ant Build</b>
      launch shortcut is used, it searches for the buildfile
      to execute starting in the folder of the selected resource and working its way
      upwards (some will recognize this as Ant's &quot;-find&quot;
      feature).
      The names of buildfiles to search for are specified in the Ant preference page.</td>
  </tr>
</table>

<h3><a name="Help">Help</a></h3>
<table border="1" cellpadding="10" cellspacing="0" width="800">
  <tr>
    <td width="20%" valign="top" align="left"><b>Show in external window</b></td>
    <td width="80%" valign="top" align="left">Having trouble reading help topics
    from the Help view/tray? Use the <img border="0"
    src="images/help_external_window.png" alt="Show in external window"> <b>Show
    in external window</b> button from the toolbar to view the document in the
    full help window.
    </td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Find that topic</b></td>
    <td width="80%" valign="top" align="left">While browsing a searched topic,
    you can find out where that topic is in the table of contents by using
    the <img border="0" src="images/help_show_topic.png"
    alt="Show in table of contents"> <b>Show in table of contents</b> button in
    the toolbar.</td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Bookmarks</b></td>
    <td width="80%" valign="top" align="left">You can keep your own list of
      bookmarks to pages in help books. Create a bookmark with the
      <img border="0" src="images/help_bookmarks_1.png" alt="Bookmark document icon">
      <b>Bookmark Document</b> button on the toolbar of the Help browser. The
      bookmarks show up in the <img border="0" src="images/help_bookmarks_2.png"
      alt="Bookmarks icon"> <b>Bookmarks</b> tab.</td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Infopops</b></td>
    <td width="80%" valign="top" align="left">If you prefer the yellow pop-ups
      (infopops) used in previous releases for context-sensitive help, you can
      configure Help to use these instead of the Help view/tray from the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.help.ui.browsersPreferencePage)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Help</strong></a>
      
      preference page.</td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Cheat Sheets</b></td>
    <td width="80%" valign="top" align="left">Cheat sheets provide step by step 
    guidance on how to perform common tasks. To see what cheat sheets exist use 
    the
    
    <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.cheatsheets.openCheatSheet")'>
    <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
    <strong>Help &gt; Cheat Sheets...</strong></a>
    
    menu item. This menu item may not appear in all perspectives.</td>
  </tr>
  <tr>
    <td width="20%" valign="top" align="left"><b>Cheat Sheet State</b></td>
    <td width="80%" valign="top" align="left">A cheat sheet will remember which 
    steps you have performed even if you close the cheat sheet view, open 
    another cheat sheet or exit Eclipse.</td>
  </tr>
</table>

<h3><a name="CVS">Team - CVS</a></h3>

<table border="1" cellpadding="10" cellspacing="0" width="800">
  <tr> 
    <td width="20%" valign="top" align="left"><b>CVS Watch/Edit</b></td>
    <td width="80%" valign="top" align="left">The &quot;edit&quot; portion of 
      CVS Watch/Edit is now supported. Through settings on the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.team.cvs.ui.WatchEditPreferencePage)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Team &gt; CVS &gt; Watch/Edit</strong></a>
      
      preference page (which must be set before the projects 
      are added to your workspace), you can choose to automatically notify the 
      CVS server whenever you start to edit a file. In turn, the CVS server will 
      notify others on the watch list for that file. When you go to edit a file, 
      you are warned if there are others editing the same file. <b>Team &gt; Show 
      Editors</b> on a file's context menu lists everyone currently working on 
      the file. There are also <b>Team &gt; Edit</b> and <b>Unedit</b> commands.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Working set for imported team 
      projects</b></td>
    <td width="80%" valign="top" align="left">There is an option to create a working 
      set for projects imported into the workspace via
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.file.import(importWizardId=org.eclipse.team.ui.ProjectSetImportWizard)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>Import &gt; Team Project Set</strong></a>.
      
      This works for all types of repositories. 
      <p><img border="0" src="images/team-project-set.png"  alt="Team project set import dialog"></p></td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>CVS now supports working sets</b></td>
    <td width="80%" valign="top" align="left">Users can now define working sets 
      which will limit the number of projects shown in the CVS Repositories view.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Comparing different versions</b></td>
    <td width="80%" valign="top" align="left">Select any folder or file in the 
      CVS Repositories view and choose <b>Compare With</b> from context menu to 
      compare it against another version, branch, or date.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Restoring deleted files from 
      CVS</b></td>
    <td width="80%" valign="top" align="left">Deleted files can now be queried 
      and restored from the CVS repository using the <b>Team &gt; Restore from 
      Repository </b>command, which is available on CVS projects and folders.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Pin a Synchronization</b> </td>
    <td width="80%" valign="top" align="left">You can now have multiple synchronizations 
      defined and available in the Synchronize View. Use the pin toolbar button 
      in the Synchronize View to pin a synchronization. The next time you synchronize 
      a new synchronization will be created. This way you can synchronize different 
      sets of resources.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Checkout Wizard</b> </td>
    <td width="80%" valign="top" align="left">You can now checkout projects in 
      one easy step via the
      
      <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.file.import(importWizardId=org.eclipse.team.cvs.ui.newProjectCheckout)")'>
      <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
      <strong>File &gt; Import &gt; Checkout projects from CVS wizard</strong></a>.
      
      This also allows checking out projects from a CVS server that doesn't support 
      browsing of its contents.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Browsing changes by CVS change 
      set</b> </td>
    <td width="80%" valign="top" align="left">You can browse a set of changes 
      shown in the Synchronize View grouped logically by author, comment, and 
      date. Enable the layout by clicking on the Change Set <img alt="Picture of the change set toolbar button" src="images/changelog-obj.gif"> 
      toolbar button. This layout can be used in the Incoming mode when synchronizing 
      and when comparing.</td>
  </tr>
  <tr> 
    <td valign="top" align="left"><p><strong>Group outgoing changes</strong></p>
      </td>
    <td valign="top" align="left">You can group outgoing changes into change sets 
      in the Synchronize View. To enable this, switch to Outgoing mode or Both mode and choose
      the Change Set model <img alt="Picture of the change set toolbar button" src="images/changelog-obj.gif"> from the model
      selection dropdown in the toolbar. 
      You can then create outgoing change sets and assign changes to them.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Schedule a synchronize</b> </td>
    <td width="80%" valign="top" align="left">You can schedule that a certain 
      synchronization run periodically. You can schedule any CVS synchronization 
      from within the Synchronize View via the <b>Schedule...</b> action in the 
      view's dropdown menu.</td>
  </tr>
  <tr> 
    <td width="20%" valign="top" align="left"><b>Want to release changes to an 
      existing branch</b> </td>
    <td width="80%" valign="top" align="left">If you have changes in your workspace 
      that you would like to commit to another branch than the one currently connected 
      to, you can run the <b>Team &gt; Switch to Another Branch or Version</b> command 
      and switch to another branch. This operation won't modify the changed files 
      and you can then commit them to the other branch.</td>
  </tr>
  <tr> 
    <td valign="top" align="left" width="20%"><strong>Sharing your CVS lineup 
      with others</strong></td>
    <td valign="top" align="left" width="80%"><p CLASS="Head">You can save the 
        list of projects shared with CVS into a team project set. This provides 
        an easy way of re-creating your workspace with shared CVS projects. </p>
      <ol>
        <li>Once you have checked out the set of projects from the CVS repository, 
          select
          
          <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.file.export")'>
          <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
          <strong>File &gt; Export</strong></a>
          
          from the main menu.</li>
        <li> Select <strong>Team Project Set</strong> from the list and then select 
          the projects to be exported. The generated file can be shared with your 
          team to allow quick setups of your development environment. </li>
        <li>To import the project set select
        
          <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.file.import")'>
          <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
          <strong>File &gt; Import</strong></a>
          
          and select <strong>Team Project Set</strong>. The projects will be checked 
          out of CVS and a repository location will automatically be created.</li>
      </ol></td>
  </tr>
  <tr> 
    <td valign="top" align="left" width="20%"><strong>Reverting a managed CVS 
      file that was edited, but not committed</strong></td>
    <td valign="top" align="left" width="80%"><p CLASS="Head">There are a three 
        ways of doing this:</p>
      <ol>
        <li>Select the file and from the context menu select <b> Team 
          &gt; Revert to Base</b>.</li>
      </ol>
      <p>or</p>  
      <ol>
        <li>Select the file and from the context menu select <b> Replace With 
          &gt; Latest from HEAD</b>.</li>
      </ol>
      <p>or</p>
      <ol>
        <li>Select the file or a parent folder and from the context menu select 
          <strong>Team &gt; Synchronize with Repository</strong>.</li>
        <li>Next switch to incoming/outgoing mode using the toolbar button in 
          the view.</li>
        <li>Select the file and from the context menu select <strong>Override 
          and Update</strong>.</li>
      </ol></td>
  </tr>
  <tr> 
    <td valign="top" align="left" width="20%"> <b>Show ancestor pane in 3-way 
      compares</b> </td>
    <td valign="top" align="left" width="80%"> Whenever a CVS synchronization 
      results in a conflict, it is helpful to view the common ancestor on which 
      the two conflicting versions are based. 
      <p> You can view the common ancestor by toggling the <b>Show Ancestor Pane</b> 
        button in the compare viewer's local toolbar.</p> 
      <p> <img src="images/compare-showancestor.png"  alt="Show Ancestor Pane button in compare viewer" border="0"> </p>
      <p> If you always want to have the ancestor pane open automatically for 
        conflicts, you can check the option <b>Initially show ancestor pane</b> 
        on the Text Compare tab of the Compare/Patch preference page.</p> </td>
  </tr>
  <TR> 
    <TD width="20%" valign="top" align="left"><B>Merge in Compare editor</B> </TD>
    <TD width="80%" valign="top" align="left">You can merge incoming changes in 
      the compare editor with one click. Hover over the small square in the middle 
      of the line connecting two ranges of an incoming or conflicting change. 
      A button appears that allows you to accept the change. 
      <P><IMG border="0" src="images/compare-merge-button.png" alt="Merge button" ></P>
      <P>Note that for this the option <B>Connect ranges with single line</B> 
        on the

        <a class="command-link" href='javascript:executeCommand("org.eclipse.ui.window.preferences(preferencePageId=org.eclipse.compare.internal.ComparePreferencePage)")'>
        <img src="PLUGINS_ROOT/org.eclipse.help/command_link.png">
        <strong>General &gt; Compare/Patch</strong></a>
        
        <strong>&gt; Text Compare</strong> preference page has to be enabled.</P></TD>
  </TR>
  <TR> 
    <TD valign="top" align="left"><p><strong>Content assist for branching and 
        merging</strong></p>
      </TD>
    <TD valign="top" align="left">When branching and merging with CVS, you can 
      use content assist in the tag fields to help select an appropriate tag. 
      For instance, when branching, you can use content assist to pick a tag from 
      the list of branch tags that exist on the other projects in your workspace. 
      When merging, you can use content assist to pick the branch that contains 
      the changes you are merging. The merge wizard will also try to pick the 
      proper start tag for you so you do not have to pick it manually.</TD>
  </TR>
  <TR>
    <TD valign="top" align="left"><p><strong>Filtering in tag selection dialogs</strong></p>
      </TD>
    <TD valign="top" align="left">There are several CVS operations that allow 
      you to specify a tag (e.g. Replace With Branch or Version, Compare 
      With Branch or Version, Checkout, etc.). These dialogs now allow you to 
      type in part of the tag name (or simple name filters using the * and ? wildcard 
      characters) and display all the tags that match what you have typed so far. 
      This greatly simplifies finding the desired tag when performing these operations.</TD>
  </TR>
</table>
</body>
</o:MultiLanguage>
</html>

