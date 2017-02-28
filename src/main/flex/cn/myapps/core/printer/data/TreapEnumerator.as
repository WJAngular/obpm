package main.flex.cn.myapps.core.printer.data
{
	/**
	 *控件枚举对象 
	 * 
	 */	
	public class TreapEnumerator
	{
		// the treap uses the stack to order the nodes 
	    private var pStack:Array=null;//<TreapNode> 
	    // return the keys 
	    private var keys:Boolean; 
	    // return in ascending order (true) or descending (false) 
	    private var ascending:Boolean; 
	    
	    // key 
	    private var ordKey:IComparable; 
	    // the data or value associated with the key 
	    private var objValue:Object; 
	    
	
	    public function Key():IComparable
	    { 
	        return ordKey;
	    } 
	    ///<summary> 
	    ///Data 
	    ///</summary> 
	    public function Value():Object
	    { 
	        return objValue; 
	    } 
	    

	    ///<summary> 
	    /// Determine order, walk the tree and push the nodes onto the stack 
	    ///</summary> 
	    public function TreapEnumerator(tnode:TreapNode,keys:Boolean,ascending:Boolean) 
	    { 
	        
	    	pStack = new Array();//<TreapNode> 
	        this.keys = keys; 
	        this.ascending = ascending; 
	        
	        // find the lowest node 
	        if ((ascending)) { 
	            while ((tnode != null)) { 
	            	pStack.push(tnode); 
	                tnode = tnode.left; 
	            } 
	        } 
	        else { 
	            // find the highest or greatest node 
	            while ((tnode != null)) { 
	            	pStack.push(tnode); 
	                tnode = tnode.right; 
	            } 
	        } 
	        
	    } 
	    ///<summary> 
	    /// HasMoreElements 
	    ///</summary> 
	    public function HasMoreElements():Boolean 
	    { 
	        return (pStack.length > 0); 
	    } 
	    ///<summary> 
	    /// NextElement 
	    ///</summary> 
	    public function NextElement():Object 
	    { 
	        var tn:TreapNode=null;
	        if ((pStack.length == 0)) { 
	            //throw new TreapException("Element not found"); 
	        } 
	        
	        // the top of stack will always have the next item 
	        // get top of stack but don't remove it as the next nodes in sequence 
	        // may be pushed onto the top 
	        // the stack will be popped after all the nodes have been returned 
	        var node:TreapNode = pStack[pStack.length-1];// peek();//(TreapNode) 
	        
	        if (ascending) { 
	            // if right node is nothing, the stack top is the lowest node 
	            // if left node is nothing, the stack top is the highest node 
	            if (node.right == null) { 
	                // walk the tree 
	                tn = pStack.pop();//(TreapNode) 
	                while ((HasMoreElements()) && (tn==(pStack[pStack.length-1]).right)) { 
	                    tn = pStack.pop(); 
	                } 
	            } 
	            else { 
	                // find the next items in the sequence 
	                // traverse to left; find lowest and push onto stack 
	                tn = node.right; 
	                while ((tn != null)) { 
	                	pStack.push(tn); 
	                    tn = tn.left;
	                } 
	            } 
	        } 
	        // descending 
	        else if (node.left == null) { 
	            // walk the tree 
	            tn = pStack.pop(); 
	            while ((HasMoreElements()) && (tn==(pStack[pStack.length-1]).left)) { 
	                tn = pStack.pop(); 
	            } 
	        } 
	        else { 
	            // find the next items in the sequence 
	            // traverse to right; find highest and push onto stack 
	            tn = node.left; 
	            while ((tn != null)) { 
	            	pStack.push(tn);
	                tn = tn.right; 
	            } 
	        } 
	        
	        // the following is for .NET compatibility (see MoveNext()) 
	        ordKey = node.Key; 
	        objValue = node.Data; 
	        
	        //Object objValue = (keys == true ? node.key : node.element); 
	        return  (keys == true ? node.Key : node.Data); //objValue; 
	        
	    } 
	    ///<summary> 
	    /// MoveNext 
	    /// For .NET compatibility 
	    ///</summary> 
	    public function MoveNext():Boolean 
	    {   
	        if (HasMoreElements()) { 
	            NextElement(); 
	            return true; 
	        } 
	        else { 
	            return false; 
	        }
	    }
	}
}