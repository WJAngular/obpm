function reload(_url)
{
   formQuery.action= _url;
   formQuery.submit();
}
function getquerySubmit()
{
   document.queryform.submit();
}

function getqueryurlSubmit(actionurl)
{
   document.queryform.action=actionurl;
   document.queryform.submit();
}

function checkDateAndForward(Start1,End1,Start2,End2,Start3,End3,url)
{
    var strStart =Start1.value;
    var strEnd =End1.value;
    if (( strStart == "" ) || ( strEnd == "" )){

    }else{ 
    var arr1 = strStart.split("-");
    var arr2 = strEnd.split("-");
    var date1 = new Date(arr1[0],parseInt(arr1[1].replace(/^0/,""),10) - 1,arr1[2]);
    var date2 = new Date(arr2[0],parseInt(arr2[1].replace(/^0/,""),10) - 1,arr2[2]);
    if(arr1[1].length == 1)
        arr1[1] = "0" + arr1[1];
    if(arr1[2].length == 1)
        arr1[2] = "0" + arr1[2];
    if(arr2[1].length == 1)
        arr2[1] = "0" + arr2[1];
    if(arr2[2].length == 1)
        arr2[2]="0" + arr2[2];
    var d1 = arr1[0] + arr1[1] + arr1[2];
    var d2 = arr2[0] + arr2[1] + arr2[2];
    if(parseInt(d1,10) > parseInt(d2,10)){
       alert('????????????????????,??????.'); 
       return false;
    } 
    else{ 

    } 
    }

    strStart =Start2.value;
    strEnd =End2.value;
    if (( strStart == "" ) || ( strEnd == "" )){

    }else{
    arr1 = strStart.split("-");
    arr2 = strEnd.split("-");
    date1 = new Date(arr1[0],parseInt(arr1[1].replace(/^0/,""),10) - 1,arr1[2]);
    date2 = new Date(arr2[0],parseInt(arr2[1].replace(/^0/,""),10) - 1,arr2[2]);
    if(arr1[1].length == 1)
        arr1[1] = "0" + arr1[1];
    if(arr1[2].length == 1)
        arr1[2] = "0" + arr1[2];
    if(arr2[1].length == 1)
        arr2[1] = "0" + arr2[1];
    if(arr2[2].length == 1)
        arr2[2]="0" + arr2[2];
    d1 = arr1[0] + arr1[1] + arr1[2];
    d2 = arr2[0] + arr2[1] + arr2[2];
    if(parseInt(d1,10) > parseInt(d2,10)){
       alert('????????????????????,??????.'); 
       return false;
    } 
    else{ 

    }
    }

    strStart =Start3.value;
    strEnd =End3.value;
    if (( strStart == "" ) || ( strEnd == "" )){
       getqueryurlSubmit(url);
       return true;
    }else{
    arr1 = strStart.split("-");
    arr2 = strEnd.split("-");
    date1 = new Date(arr1[0],parseInt(arr1[1].replace(/^0/,""),10) - 1,arr1[2]);
    date2 = new Date(arr2[0],parseInt(arr2[1].replace(/^0/,""),10) - 1,arr2[2]);
    if(arr1[1].length == 1)
        arr1[1] = "0" + arr1[1];
    if(arr1[2].length == 1)
        arr1[2] = "0" + arr1[2];
    if(arr2[1].length == 1)
        arr2[1] = "0" + arr2[1];
    if(arr2[2].length == 1)
        arr2[2]="0" + arr2[2];
    d1 = arr1[0] + arr1[1] + arr1[2];
    d2 = arr2[0] + arr2[1] + arr2[2];
    if(parseInt(d1,10) > parseInt(d2,10)){
       alert('????????????????????,??????.'); 
       return false;
    } 
    else{ 
       getqueryurlSubmit(url);
       return true;
    }
    }
}

function checkDateAndForward2(Start1,End1,url)
{
    var strStart =Start1.value;
    var strEnd =End1.value;
    if (( strStart == "" ) || ( strEnd == "" )){
         alert('????????????????????????????,??????.'); 
    }else{ 
    var arr1 = strStart.split("-");
    var arr2 = strEnd.split("-");
    var date1 = new Date(arr1[0],parseInt(arr1[1].replace(/^0/,""),10) - 1,arr1[2]);
    var date2 = new Date(arr2[0],parseInt(arr2[1].replace(/^0/,""),10) - 1,arr2[2]);
    if(arr1[1].length == 1)
        arr1[1] = "0" + arr1[1];
    if(arr1[2].length == 1)
        arr1[2] = "0" + arr1[2];
    if(arr2[1].length == 1)
        arr2[1] = "0" + arr2[1];
    if(arr2[2].length == 1)
        arr2[2]="0" + arr2[2];
    var d1 = arr1[0] + arr1[1] + arr1[2];
    var d2 = arr2[0] + arr2[1] + arr2[2];
    if(parseInt(d1,10) > parseInt(d2,10)){
       alert('????????????????????,??????.'); 
       return false;
    } 
    else{ 
        getqueryurlSubmit(url);
       return true;
    } 
    }
}