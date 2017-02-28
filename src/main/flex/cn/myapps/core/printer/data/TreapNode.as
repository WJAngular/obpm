package main.flex.cn.myapps.core.printer.data
{
	/**
	 *二叉树 节点
	 * 
	 */	
	public class TreapNode
    {
            // Constructors
//        TreapNode()
//        {
//        	// IComparable theKey,Object pObj 
//            //this(theKey, pObj,null, null );
//        }


        // Friendly data; accessible by other package routines
        public var Key:IComparable;      // The data in the node
        public var Data:Object;      // The data in the node
        public var left:TreapNode;         // Left child
        public var right:TreapNode;        // Right child
        //int        priority;     // Priority
        public var height:int;//平衡树的高度
        
        public function TreapNode(theKey:IComparable,pObj:Object,lt:TreapNode,rt:TreapNode)
        {
        	Key		=	theKey;
        	Data	=	pObj;
            left    = lt;
            right   = rt;
            height		=	0;
            //priority= randomObj.randomInt( );
        }


        public function RotateLeft():TreapNode 
        { 
            
            var temp:TreapNode = right; 
            right = right.left; 
            temp.left = this; 
            this.calculateHeight();
            temp.calculateHeight();
            return temp; 
            
        } 
        ///<summary> 
        /// RotateRight 
        /// Rebalance the tree by rotating the nodes to the right 
        ///</summary> 
        public function RotateRight():TreapNode 
        { 
            
            var temp:TreapNode = left; 
            left = left.right; 
            temp.right = this;
            
            this.calculateHeight();
            temp.calculateHeight();
            return temp;
        } 
        
        public function calculateHeight():void
        {
        	//计算高度
        	var iLeft:int=0;
        	var iRight:int=0;
        	if (left!=null){
        		iLeft=left.height;
        	}
        	if (right!=null){
        		iRight=right.height;
        	}
        	this.height=Math.max(iLeft,iRight)+1;
        }
        	
        
        public function DeleteRoot():TreapNode 
        { 
            
            var temp:TreapNode; 
            
            if (left == null){
                return right; 
            } 
            
            if (right == null){ 
                return left; 
            } 
            
            //if ((left.priority < right.priority)) { 
          	if (left.height > right.height) {
                temp = RotateRight(); 
                temp.right = DeleteRoot(); 
            }
            else  { 
                temp = RotateLeft(); 
                temp.left = DeleteRoot(); 
            }
          	
          	//计算height
          	temp.calculateHeight();
          	
            return temp;
            
        } 
    }
}