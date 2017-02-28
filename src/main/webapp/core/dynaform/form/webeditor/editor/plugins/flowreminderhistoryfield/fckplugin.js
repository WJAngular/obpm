/*
 * FCKeditor - The text editor for Internet - http://www.fckeditor.net
 * Copyright (C) 2003-2010 Frederico Caldeira Knabben
 *
 * == BEGIN LICENSE ==
 *
 * Licensed under the terms of any of the following licenses at your
 * choice:
 *
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 *
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 *
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 *
 * == END LICENSE ==
 *
 * Plugin to insert "flowreminderhistoryfield" in the editor.
 */

// Register the related command.
function getUrl(){
	var url;
	var moduleid = parent.document.getElementById('moduleid').value;
	var applicationid = parent.document.getElementById('application').value;
	var formid = parent.document.forms[0].elements['content.id'].value;
	url = 'myapps_flowreminderhistoryfield.jsp?';
	url += '&moduleid=' + moduleid + '&application='+applicationid+'&formid='+formid;
	return FCKPlugins.Items['flowreminderhistoryfield'].Path+url;
}
FCKCommands.RegisterCommand( 'flowreminderhistoryfield', new FCKDialogCommand( 'flowreminderhistoryfield', FCKLang.flowreminderhistoryfieldtitle, getUrl(), 600, 350 ) ) ;

// Create the "Plaholder" toolbar button.
var oflowreminderhistoryfieldItem = new FCKToolbarButton( 'flowreminderhistoryfield', FCKLang.flowreminderhistoryfield ) ;
oflowreminderhistoryfieldItem.IconPath = FCKPlugins.Items['flowreminderhistoryfield'].Path + 'flowreminderhistoryfield.png' ;

FCKToolbarItems.RegisterItem( 'flowreminderhistoryfield', oflowreminderhistoryfieldItem ) ;


// The object used for all flowreminderhistoryfield operations.
var FCKflowreminderhistoryfield = new Object() ;

// Add a new placeholder at the actual selection.
FCKflowreminderhistoryfield.Add = function( name )
{
	var oSpan = FCK.InsertElement( 'IMG' ) ;
	this.SetupSpan( oSpan, name ) ;
}

FCKflowreminderhistoryfield.SetupSpan = function( IMG, name )
{
	IMG.innerHTML = '[[ ' + name + ' ]]' ;

	IMG.style.backgroundColor = '#ffff00' ;
	IMG.style.color = '#000000' ;

	if ( FCKBrowserInfo.IsGecko )
		IMG.style.cursor = 'default' ;

	IMG._fckplaceholder = name ;
	IMG.contentEditable = false ;

	// To avoid it to be resized.
	IMG.onresizestart = function()
	{
		FCK.EditorWindow.event.returnValue = false ;
		return false ;
	}
}

// On Gecko we must do this trick so the user select all the IMG when clicking on it.
FCKflowreminderhistoryfield._SetupClickListener = function()
{
	FCKflowreminderhistoryfield._ClickListener = function( e )
	{
		if ( e.target.tagName.toUpperCase() == 'IMG' && e.target.type.toLowCase()=="flowreminderhistoryfield")
			FCKSelection.SelectNode( e.target );
	}

	FCK.EditorDocument.addEventListener( 'click', FCKflowreminderhistoryfield._ClickListener, true ) ;
}

// Open the flowreminderhistoryfield dialog on double click.
FCKflowreminderhistoryfield.OnDoubleClick = function( IMG )
{
	if ( IMG.tagName.toUpperCase() == 'IMG' && IMG.classname == 'cn.myapps.core.dynaform.form.ejb.flowreminderhistoryfield')
		FCKCommands.GetCommand( 'flowreminderhistoryfield').Execute() ;
}

FCK.RegisterDoubleClickHandler( FCKflowreminderhistoryfield.OnDoubleClick, 'IMG' ) ;

// Check if a Placholder name is already in use.
FCKflowreminderhistoryfield.Exist = function( name )
{
	var aSpans = FCK.EditorDocument.getElementsByTagName( 'IMG' ) ;

	for ( var i = 0 ; i < aSpans.length ; i++ )
	{
		if ( aSpans[i]._fckplaceholder == name )
			return true ;
	}

	return false ;
}

if ( FCKBrowserInfo.IsIE )
{
	FCKflowreminderhistoryfield.Redraw = function()
	{
		if ( FCK.EditMode != FCK_EDITMODE_WYSIWYG )
			return ;

		var aPlaholders = FCK.EditorDocument.body.innerText.match( /\[\[[^\[\]]+\]\]/g ) ;
		if ( !aPlaholders )
			return ;

		var oRange = FCK.EditorDocument.body.createTextRange() ;

		for ( var i = 0 ; i < aPlaholders.length ; i++ )
		{
			if ( oRange.findText( aPlaholders[i] ) )
			{
				var sName = aPlaholders[i].match( /\[\[\s*([^\]]*?)\s*\]\]/ )[1] ;
				oRange.pasteHTML( '<IMG style="color: #000000; background-color: #ffff00" contenteditable="false" _fckplaceholder="' + sName + '">' + aPlaholders[i] + '</IMG>' ) ;
			}
		}
	}
}
else
{
	FCKflowreminderhistoryfield.Redraw = function()
	{
		if ( FCK.EditMode != FCK_EDITMODE_WYSIWYG )
			return ;

		var oInteractor = FCK.EditorDocument.createTreeWalker( FCK.EditorDocument.body, NodeFilter.SHOW_TEXT, FCKflowreminderhistoryfield._AcceptNode, true ) ;

		var	aNodes = new Array() ;

		while ( ( oNode = oInteractor.nextNode() ) )
		{
			aNodes[ aNodes.length ] = oNode ;
		}

		for ( var n = 0 ; n < aNodes.length ; n++ )
		{
			var aPieces = aNodes[n].nodeValue.split( /(\[\[[^\[\]]+\]\])/g ) ;

			for ( var i = 0 ; i < aPieces.length ; i++ )
			{
				if ( aPieces[i].length > 0 )
				{
					if ( aPieces[i].indexOf( '[[' ) == 0 )
					{
						var sName = aPieces[i].match( /\[\[\s*([^\]]*?)\s*\]\]/ )[1] ;

						var oSpan = FCK.EditorDocument.createElement( 'IMG' ) ;
						FCKflowreminderhistoryfield.SetupSpan( oSpan, sName ) ;

						aNodes[n].parentNode.insertBefore( oSpan, aNodes[n] ) ;
					}
					else
						aNodes[n].parentNode.insertBefore( FCK.EditorDocument.createTextNode( aPieces[i] ) , aNodes[n] ) ;
				}
			}

			aNodes[n].parentNode.removeChild( aNodes[n] ) ;
		}

		FCKflowreminderhistoryfield._SetupClickListener() ;
	}

	FCKflowreminderhistoryfield._AcceptNode = function( node )
	{
		if ( /\[\[[^\[\]]+\]\]/.test( node.nodeValue ) )
			return NodeFilter.FILTER_ACCEPT ;
		else
			return NodeFilter.FILTER_SKIP ;
	}
}

FCK.Events.AttachEvent( 'OnAfterSetHTML', FCKflowreminderhistoryfield.Redraw ) ;

// We must process the IMG tags to replace then with the real resulting value of the placeholder.
FCKXHtml.TagProcessors['IMG'] = function( node, htmlNode )
{
	if ( htmlNode._fckplaceholder )
		node = FCKXHtml.XML.createTextNode( '[[' + htmlNode._fckplaceholder + ']]' ) ;
	else
		FCKXHtml._AppendChildNodes( node, htmlNode, false ) ;

	return node ;
}
