package cn.myapps.core.macro.editor
{
	import flash.text.TextField;
	import flash.text.TextFormat;
	

	public class Formatter
	{
	// max length for comment and block comment string is 2
	public var commentSeq:String;
	public var blockCommentOn:String;
	public var blockCommentOff:String;
	// wordlist property names are keywords, values are TextFormat objects
	public var wordlist:Object;
	// delimiters property names are delimiting characters with Boolean true values
	public var delimiters:Object;
	
	public var commFormat:TextFormat;
	public var strFormat:TextFormat;
	
	/* formats for keyword groups are created dynamically */
	
	function Formatter(){}
	
	function format(display_txt:TextField)
	{
		var word,ch:String;
		var tf:TextFormat;
		
		var dqB:Boolean=false;
		var sqB:Boolean=false;
		var bcB:Boolean = false;
		
		var all=display_txt.text;
		var allLen=all.length;
		var bcon0=blockCommentOn.charAt(0);
		var bcon1=blockCommentOn.charAt(1);
		var bcof0=blockCommentOff.charAt(0);
		var bcof1=blockCommentOff.charAt(1);
		var coms0=commentSeq.charAt(0);
		var coms1=commentSeq.charAt(1);
		
		word="";
		
/////		
		var df:TextFormat = new TextFormat();
		df.color = 0x000000;
		df.bold = false;
		
		display_txt.setTextFormat(df);
////		

		try{
			for (var j=0; j<allLen; j++) {
				ch=all.charAt(j);
				// if there is blockComment
				if (bcon0!="") {
					// if blockComment is opened
					if (bcB!=false) {
						if (ch==bcof0) {
							if (all.charAt(j+1)==bcof1) {
								if (bcB) {
									display_txt.setTextFormat(commFormat, j+blockCommentOff.length);
									bcB=false;
								}
							}
						}
						continue;
					}
					// if its block comment
					if (ch==bcon0) {
						if (all.charAt(j+1)==bcon1) {
							bcB=j;
							j++;
							continue;
						}
					}
				}
				
				// if its comment
				if ((!dqB && ch==coms0) || coms0=="") {
					if (all.charAt(j+1)==coms1 || coms1=="") {
						var cB=j;
						while (ch!="\r" && j!=allLen) {
							j++;
							ch=all.charAt(j);
						}
						if (cB) {
							display_txt.setTextFormat(commFormat, j);
						}
						continue;
					}
				}
				//if double quotes start
				if (ch=="\"" && (all.charAt(j-1)!="\\" || !(all.charAt(j-1)=="\\" && all.charAt(j-2)!="\\"))) {
					if (dqB==false) dqB=j;
					else {
						if (dqB) {
							display_txt.setTextFormat(strFormat,j+1);
							dqB=false;
						}
					}
					continue;
				}
				//if single quotes start
				if (ch=="\'" && (all.charAt(j-1)!="\\" || !(all.charAt(j-1)=="\\" && all.charAt(j-2)!="\\"))) {
					if (sqB==false) sqB=j;
					else {
						if (sqB) {
							display_txt.setTextFormat(strFormat,j+1);
							sqB=false;
						}
					}
					continue;
				}
				// if its not double or single quoted
				if (dqB==false && sqB==false) {
					
	//				trace("delimiters["+ch+"]-->"+delimiters[ch]);
					if (delimiters[ch]) {
						if (word!="") {
							var e=j;
							tf=wordlist[word];
							if (tf) display_txt.setTextFormat(tf,e-word.length,e);
							word="";
						}
					} else {
						word+=ch;
					}
	
				}
			}
		} catch(e:Error) {
			trace("(" + ch + ") " + e);
		}
		
/*		
		tf=wordlist[word];
		
		if (tf) {
//			display_txt.setTextFormat(tf, j, j+word.length);
		}
		else {
			display_txt.setTextFormat(iniFormat, j-1);
		}
*/	
	}

	}
}