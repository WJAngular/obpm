package main.flex.cn.myapps.core.printer.data
{
	/**
	 * 
	 * 利用平衡树 储蓄画板上添加的控件
	 * 
	 */
    public class Treap
    {
    	//private Random rndPriority = new Random();
    	
    	private var intCount:int; 

    	// random priority to keep the treap balanced 
    	// the number of key-and-value pairs contained in the treap 
    	// used for quick comparisons 
    	//private int intHashCode = rndPriority.Next(); 
    	private var treapTree:TreapNode; 
    	private var boolKeyFound:Boolean; 
    	public var KeyMaxLen:int; 
    	// identIfies the owner of the treap 
    	// the treap 

    	//代表Key的字符串最长为多少，一般不用 



    	public function Add_Treap_IfNone(key:IComparable):Treap 
    	{ 
    	    var pTreap:Treap =Treap(this.find(key)); 
    	    if (pTreap == null) { 
    	        pTreap = new Treap(); 
    	        this.insert(key, pTreap); 
    	    } 
    	    return pTreap; 
    	} 

    	///<summary> 
    	/// Add 
    	/// args: ByVal key As IComparable, ByVal data As Object 
    	/// key is object that implements IComparable interface 
    	///</summary> 
    	public function insert(key:IComparable,data:Object):TreapNode 
    	{ 
    		//S_File_Text.Write("Treap.txt", this.Size()+"\r\n",true);
    	    if ((key == null || data == null)) {
    	    	return null;
    	        //throw new TreapException("Treap key and data must not be Nothing"); 
    	    } 
    	    
    	    // create New node 
    	    var node:TreapNode = new TreapNode(null,null,null,null);
    	    if (node == null) { 
    	    	return null;
    	    	//throw new TreapException("Unable to create Treap Node"); 
    	    } 
    	    
    	    node.Key = key; 
    	    node.Data = data; 
    	    // generate random priority
    	    node.height=1;
    	    //node.priority = rndPriority.Next(); 
    	    boolKeyFound = false; 
    	    
    	    // Insert node into treapTree 
    	    treapTree = InsertNode(node, treapTree); 
    	    
    	    if ((boolKeyFound)) {
    	    	//Throw New TreapException("A Node with the same key already exists") 
    	    } 
    	    else { 
    	        intCount = intCount + 1; 
    	    } 
    	    
    	    return treapTree; 
    	}

		/**
		 * /<summary> 
    	/ InsertNode 
    	/ inserts a node into the tree - note recursive method 
    	/ this method rebalances the tree using the priorities 
    	/ 
    	/ Note: The lower the number, the higher the priority 
    	/<summary> 
		 * 递归方法
		 * @param node
		 * @param tree
		 * @return 
		 * 
		 */		
    	private function InsertNode(node:TreapNode,tree:TreapNode):TreapNode 
    	{ 
    	    
    	    if ((tree == null)) { 
    	        return node; 
    	    } 
    	    
    	    var result:int= node.Key.compareTo(tree.Key); //连个组件类别比较 
			
    	    if ((result < 0)) { //less than zero if this object is smaller;
    	        tree.left = InsertNode(node, tree.left); 
    	        if (tree.right!=null){
	    	        if (tree.left.height - tree.right.height>1) { 
	    	            tree = tree.RotateRight();
	    	        }
    	        }else{
    	        	tree = tree.RotateRight();
    	        }
    	    } 
    	    else if ((result > 0)) { //greater than zero if this object is larger.
    	        tree.right = InsertNode(node, tree.right); 
    	        if (tree.left!=null){
	    	        if (tree.right.height - tree.left.height>1) { 
	    	            tree = tree.RotateLeft();
	    	        }
    	        }else{
    	        	tree = tree.RotateLeft();
    	        }
    	    } 
    	    else {//没有左节点，即当前node是根节点
    	        boolKeyFound = true; 
    	        tree.Data = node.Data; 
    	    }
    	    //设置 height属性
    	    tree.calculateHeight();
    	    
    	    return tree; 
    	    
    	} 

    	///<summary> 
    	/// GetData 
    	/// Gets the data associated with the specified key 
    	///<summary> 
    	public function find(key:IComparable):Object 
    	{ 
    	    
    	    var treeNode:TreapNode = treapTree; 
    	    var result:int; 
    	    
    	    //Dim treeNode_old As TreapNode 
    	    while (((treeNode != null))) { 
    	        result = key.compareTo(treeNode.Key); 
    	        if (result == 0) { 
    	            return treeNode.Data; 
    	        } 
    	        
    	        //treeNode_old = treeNode 
    	        if ((result < 0)) { 
    	            treeNode = treeNode.left; 
    	        } 
    	        else { 
    	            treeNode = treeNode.right; 
    	        } 
    	    } 
    	    
    	    //Throw New TreapException("Treap key was not found") 
    	    
    	    return null; 
    	    
    	} 
    	///<summary> 
    	/// GetMinKey 
    	/// Returns the minimum key value 
    	///<summary> 
    	public function GetMinKey():IComparable
    	{ 
    	    
    	    var treeNode:TreapNode = treapTree; 
    	    
    	    if ((treeNode == null)) { 
    	        //throw new TreapException("Treap is empty"); 
    	        return null; 
    	    } 
    	    
    	    while ((treeNode.left != null)) { 
    	        treeNode = treeNode.left; 
    	    } 
    	    
    	    return treeNode.Key; 
    	    
    	} 
    	///<summary> 
    	/// GetMaxKey 
    	/// Returns the maximum key value 
    	///<summary> 
    	public function GetMaxKey():IComparable 
    	{ 
    	    
    	    var treeNode:TreapNode = treapTree; 
    	    
    	    if ((treeNode == null)) { 
    	        //throw new Exception("Treap is empty"); 
    	        return null; 
    	    } 
    	    
    	    while ((treeNode.right != null)) { 
    	        treeNode = treeNode.right; 
    	    } 
    	    
    	    return treeNode.Key; 
    	    
    	} 
    	///<summary> 
    	/// GetMinValue 
    	/// Returns the object having the minimum key value 
    	///<summary> 
    	public function GetMinValue():Object 
    	{ 
    	    return find(GetMinKey()); 
    	} 
    	///<summary> 
    	/// GetMaxValue 
    	/// Returns the object having the maximum key 
    	///<summary> 
    	public function GetMaxValue():Object
    	{ 
    	    return find(GetMaxKey()); 
    	} 
    	///<summary> 
    	/// GetEnumerator 
    	///<summary> 
    	public function GetEnumerator():TreapEnumerator 
    	{ 
    	    return Elements(true); 
    	} 
    	///<summary> 
    	/// Keys 
    	/// If ascending is True, the keys will be returned in ascending order, else 
    	/// the keys will be returned in descending order. 
    	///<summary> 
//    	public function Keys():TreapEnumerator 
//    	{ 
//    	    return Keys(true); 
//    	}
    	
    	public function Keys(ascending:Boolean):TreapEnumerator 
    	{ 
    	    return new TreapEnumerator(treapTree, true, ascending); 
    	}
    	
    	///<summary> 
    	/// Values 
    	/// .NET compatibility 
    	///<summary> 
    	public function Values():TreapEnumerator 
    	{ 
    	    return Elements(true); 
    	} 
    	///<summary> 
    	/// Elements 
    	/// Returns an enumeration of the data[control] objects. 
    	/// If ascending is true, the objects will be returned in ascending order, 
    	/// else the objects will be returned in descending order. 
    	///<summary> 
		/**
		 * 传递的参数ascending 为TRUE返回递增的控件枚举对象，为FALSE返回递减的控件枚举对象
		 * @param ascending
		 * @return 
		 * 
		 */		
    	public function Elements(ascending:Boolean):TreapEnumerator 
    	{ 
    	    return new TreapEnumerator(treapTree, false, ascending); 
    	} 
    	///<summary> 
    	/// IsEmpty 
    	///<summary> 
    	public function IsEmpty():Boolean 
    	{ 
    	    return (treapTree == null); 
    	} 
    	///<summary> 
    	/// Remove 
    	/// removes the key and Object 
    	///<summary> 
    	public function remove(key:IComparable):Boolean 
    	{ 
    	    
    	    boolKeyFound = false; 
    	    
    	    treapTree = Delete(key, treapTree); 
    	    
    	    if (boolKeyFound) { 
    	        intCount = intCount - 1; 
    	    } 
    	    
    	    return boolKeyFound; 
    	} 
    	///<summary> 
    	/// RemoveMin 
    	/// removes the node with the minimum key 
    	///<summary> 
    	public function RemoveMin():Object 
    	{ 
    	    
    	    // start at top 
    	    var treeNode:TreapNode = treapTree; 
    	    var prevTreapNode:TreapNode; 
    	    
    	    if ((treeNode == null)) {
    	    	return null;
    	        //throw new TreapException("Treap is null"); 
    	    } 
    	    
    	    if ((treeNode.left == null)) { 
    	        // remove top node by replacing with right 
    	        treapTree = treeNode.right; 
    	    } 
    	    else { 
    	        do { 
    	            // find the minimum node 
    	            prevTreapNode = treeNode; 
    	            treeNode = treeNode.left; 
    	        } 
    	        while ((treeNode.left != null)); 
    	        // remove left node by replacing with right node 
    	        prevTreapNode.left = treeNode.right; 
    	    } 
    	    
    	    intCount = intCount - 1; 
    	    
    	    return treeNode.Data; 
    	    
    	} 

    	///<summary> 
    	/// RemoveMax 
    	/// removes the node with the maximum key 
    	///<summary> 
    	public function RemoveMax():Object 
    	{ 
    	    
    	    // start at top 
    	    var treeNode:TreapNode = treapTree; 
    	    var prevTreapNode:TreapNode; 
    	    
    	    if ((treeNode == null)) { 
    	    	return null;
    	        //throw new TreapException("Treap is null"); 
    	    } 
    	    
    	    if ((treeNode.right == null)) { 
    	        // remove top node by replacing with left 
    	        treapTree = treeNode.left; 
    	    } 
    	    else { 
    	        do { 
    	            // find the maximum node 
    	            prevTreapNode = treeNode; 
    	            treeNode = treeNode.right; 
    	        } 
    	        while ((treeNode.right != null)); 
    	        // remove right node by replacing with left node 
    	        prevTreapNode.right = treeNode.left; 
    	    } 
    	    
    	    intCount = intCount - 1; 
    	    
    	    return treeNode.Data; 
    	    
    	} 
    	///<summary> 
    	/// Clear 
    	///<summary> 
    	public function Clear():void 
    	{ 
    	    treapTree = null; 
    	    intCount = 0; 
    	} 
    	///<summary> 
    	/// Size 
    	///<summary> 
    	public function get Size():int 
    	{ 
    	    return intCount; 
    	} 
    	///<summary> 
    	/// Delete 
    	/// deletes a node - note recursive function 
    	/// Deletes works by "bubbling down" the node until it is a leaf, and then 
    	/// pruning it off the tree 
    	///<summary> 
    	private function Delete(key:IComparable,tNode:TreapNode):TreapNode 
    	{ 
    	    
    	    if ((tNode == null)) { 
    	        return null; 
    	    } 
    	    
    	    var result:int = key.compareTo(tNode.Key); 
    	    
    	    if ((result < 0)) { 
    	        tNode.left = Delete(key, tNode.left);
    	        if (tNode.left!=null)
    	        	tNode.left.calculateHeight();
    	    } 
    	    else if ((result > 0)) { 
    	        tNode.right = Delete(key, tNode.right);
    	        if (tNode.right!=null)
    	        	tNode.right.calculateHeight();
    	    } 
    	    else { 
    	        boolKeyFound = true; 
    	        tNode = tNode.DeleteRoot();
    	        if (tNode!=null)
    	        	tNode.calculateHeight();
    	    } 
    	    
    	    return tNode; 
    	    
    	} 
    }

	
}