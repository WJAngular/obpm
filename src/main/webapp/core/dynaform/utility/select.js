function uploadFile(fieldFile){
  wx = '600px';
  wy = '300px';	

  var rtn = showframe("�ϴ�ͼƬ",contextPath +"/component/fileupload/upload.jsp");
  if (rtn == null || rtn == 'undefined') {
  }
  else if (rtn == '') {
    if (fieldFile !=null) fieldFile.value = '';
  }else{
    if (fieldFile !=null) fieldFile.value = rtn;
  }
}

function newHtml(fieldFile) {
  wx = '800px';
  wy = '500px';	

  if (fieldFile !=null && fieldFile.value != ''){
	  editHtml(fieldFile.value,fieldFile);
	  return;
  }
    
  var rtn = showframe("HTML�༭",contextPath +"/component/htmleditor/new.do?ISEDIT=TRUE&COMMAND=new");
  
  if (rtn == null || rtn == 'undefined') {
  }
  else if (rtn == '') {
    if (fieldFile !=null) fieldFile.value = '';
  }else{
    if (fieldFile !=null) fieldFile.value=rtn;
  }
}

function editHtml(htmlFile,fieldFile) {
  wx = '800px';
  wy = '500px';	

  if (fieldFile !=null && fieldFile.value != '')
	  htmlFile = fieldFile.value;

  var rtn = showframe("HTML�༭",contextPath +"/component/htmleditor/edit.do?htmlfile="+htmlFile+"&ISEDIT=TRUE");

  if (rtn == null || rtn == 'undefined') {
  }
  else if (rtn == '') {
    if (fieldFile !=null) fieldFile.value = '';
  }else{
    if (fieldFile !=null) fieldFile.value=rtn;
  }
}

function viewHtml(htmlFile,fieldFile) {
  wx = '800px';
  wy = '500px';	

  if (fieldFile !=null && fieldFile.value != ''){
	  htmlFile = fieldFile.value;
    showframe("HTML�༭",contextPath +"/component/htmleditor/view.do?htmlfile="+htmlFile);
 }else{
    alert("�ļ�Ϊ�գ��޷��鿴��");
 }
}

function newWord(fieldFile) {
  wx = '800px';
  wy = '5600px';	

  var rtn = showframe("Word�༭",contextPath +"/component/wordfield/new.do?ISEDIT=TRUE&COMMAND=new");

  if (fieldFile !=null && fieldFile.value != ''){
	  editWord(fieldFile.value);
	  return;
  }
      
  if (rtn == null || rtn == 'undefined') {
  }
  else if (rtn == '') {
    if (fieldFile !=null) fieldFile.value = '';
  }else{
    if (fieldFile !=null) fieldFile.value=rtn;
  }
}

function editWord(wordFile,fieldFile) {
  wx = '800px';
  wy = '600px';	

  if (fieldFile !=null && fieldFile.value != '')
	  wordFile = fieldFile.value;
  
  var rtn = showframe("Word�༭",contextPath +"/component/wordfield/edit.do?wordfile="+wordFile+"&ISEDIT=TRUE");
  
  if (rtn == null || rtn == 'undefined') {
  }
  else if (rtn == '') {
    if (fieldFile !=null) fieldFile.value = '';
  }else{
    if (fieldFile !=null) fieldFile.value=rtn;
  }
}

function viewWord(wordFile,fieldFile) {
  wx = '800px';
  wy = '600px';	

  if (fieldFile !=null && fieldFile.value != ''){
	  wordFile = fieldFile.value;
    showframe("Word�༭",contextPath +"/component/wordfield/view.do?wordfile="+wordFile);
  }else{
    alert("�ļ�Ϊ�գ��޷��鿴��");
 }

}