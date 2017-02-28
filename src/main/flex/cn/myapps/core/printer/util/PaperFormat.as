

package main.flex.cn.myapps.core.printer.util
{
	
	public class PaperFormat
	{
		/**/
		/*
		public static const A0 : PaperFormat = new PaperFormat([2384, 3370],"A0",[33.1,46.81],[841,1189]);
		public static const A1 : PaperFormat = new PaperFormat([1684, 2384],"A1",[23.39,33.11],[594,841]);
		public static const A2 : PaperFormat = new PaperFormat([1191, 1684],"A2",[16.54,23.39],[420,594]);
		*/
		public static const A3 : PaperFormat = new PaperFormat([842, 1191],"A3",[11.9,16.5],[297,420]);
		public static const A4 : PaperFormat = new PaperFormat([595, 842],"A4",[8.27,11.9],[210,297]);
		public static const A5 : PaperFormat = new PaperFormat([420, 595],"A5",[5.83,8.27],[148,210]);
		/*
		public static const A0R : PaperFormat = new PaperFormat([2384, 3370],"A0.Rotated",[33.1,46.81],[841,1189]);
		public static const A1R : PaperFormat = new PaperFormat([1684, 2384],"A1.Rotated",[23.39,33.11],[594,841]);
		public static const A2R : PaperFormat = new PaperFormat([1191, 1684],"A2.Rotated",[16.54,23.39],[420,594]);
		*/
		public static const A3R : PaperFormat = new PaperFormat([842, 1191],"A3.Rotated",[11.9,16.5],[297,420]);
		public static const A4R : PaperFormat = new PaperFormat([595, 842],"A4.Rotated",[8.27,11.9],[210,297]);
		public static const A5R : PaperFormat = new PaperFormat([420, 595],"A5.Rotated",[5.83,8.27],[148,210]);
		
		/* US */
		public static const LEGAL : PaperFormat = new PaperFormat([612, 1008],"Legal",[8.5,14],[215.9,355.6]);
		public static const LEGALR : PaperFormat = new PaperFormat([612, 1008],"Legal.Rotated",[8.5,14],[215.9,355.6]);

		public static const LETTER : PaperFormat = new PaperFormat([612, 792],"Letter",[8.5,11],[215.9,279.4]);
		public static const LETTERR : PaperFormat = new PaperFormat([612, 792],"Letter.Rotated",[8.5,11],[215.9,279.4]);
		
		public static const TABLOID : PaperFormat = new PaperFormat([792, 1224],"Tabloid",[11,17],[279.4,431.8]);
		public static const TABLOIDR:PaperFormat = new PaperFormat([792, 1224],"Tabloid.Rotated",[11,17],[279.4,431.8]);
				
		public function get width() : Number
		{
			return dimensions[0];
		}

		public function get height() : Number
		{
			return dimensions[1];
		}
		
		public function get scale() : Number
		{
			return height / width;
		}
		
		public static var paperFormats:Array = new Array(A3,A4,A5,LETTER,LEGAL,TABLOID);

		public var dimensions:Array;

		public var label:String = "";
		
		public var inchesSize:Array;
		
		public var mmSize:Array;
				
		public static function getPaperFormat( value:Object ) : PaperFormat
		{
			if( value is PaperFormat ) { return PaperFormat ( value ); }
			
			if( value is String )
			{
				for each (var s:PaperFormat in paperFormats )
				{
					if( s.label == ( String (value) ) )
					{
						return s;
					}
				}
			}
			return null;
		}
				
		public function get fullLabel() : String
		{
			return label + " - " + inchesSize[0] + "x" + inchesSize[1] + "\" - " + mmSize[0] + "x" + mmSize[1] + "mm";
		}
		
/**
 * 
		public function toSize():Size {
			var result:Size = null;
			for each(var size:Size in Size.sizes) {
				if(size.label == label) {
					result = size;
					break;
				}
			}	
			return result;
		}
*/	
					
		public function PaperFormat( pPixelsSize:Array, pLabel:String, pInchesSize:Array, pMmSize:Array ) : void
		{
			this.dimensions = pPixelsSize;
			this.label = pLabel;
			this.inchesSize = pInchesSize;
			this.mmSize = pMmSize;
		}
	}
}