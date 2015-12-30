
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">

    <title>车辆查询</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="<%=path%>//style.css">
	-->
	<style type="text/css">
		.tableborder{
			border:solid 1px #CCCCCC;
			border-collapse:collapse;
			font-size:12px;

		}
		.tableborder td{
			border:solid 1px #CCCCCC;
			border-collapse:collapse;
			height:20px;
			padding-left:3px;
			background-color:#fcfcfc;
			font-family:serif;
		}
		.tableborder th{
			height:26px;
			font-weight:normal;
			font-family:serif;
			border:solid 1px #CCCCCC;
			border-collapse:collapse;
			color:#184da8;
			text-align:center;
			padding-left:3px;
			line-height:26px;
			background-color: #ebecee;
			background-repeat: repeat-x;
			background-position: top;
			padding:0;
			margin:0;
			white-space:nowrap;
		}

		.tableborder tr.t1 td {background-color:#fff; }
		.tableborder tr.t2 td {background-color:#fafcff; }
		.tableborder tr.t3 td {background-color:#e0e9f9; }
		
	<!-- a标签样式 -->
	.yang  a:link,
    .yang  a:visiteid  {
    color:#FF0000;
    text-decoration:none;
    }
    .yang  a:hover,
    .yang  a:active  {
    color:#000000;
    text-decoration:underline;
    background:#FFFFFF;
    } 

		
</style>
	<script type="text/javascript" src="<%=path%>/js/jquery-1.4.3.js"></script>
  <script type="text/javascript">

  	function search(){
  		//document.form1.action = "car!search.action";
  		var mtv = $("#mt").val();
  		if(mtv=='KKBHARR'){
  		var chk_value =[];
        $('input[name="box"]:checked').each(function(){
        chk_value.push($(this).val()); });
        //alert(chk_value.length==0 ?'你还没有选择任何内容！':chk_value);
         document.form1.action = "car!search.action?kkbhArr="+chk_value;
  		}
  		 document.form1.submit();
  	}
  	
  	function query(maker){
  	 var pages=null;
  	 var rowKey=null;
  	 var mark=null;
  	 var url=null;
  	    if(maker==1){
  	      pages=$("#spage").val();
  	      url="car!search.action?page="+pages+"&mark="+mark;
  	    }else if(maker==2){//上一页
  	      mark="p";
  	      pages=$("#ppage").val();
  	      url="car!search.action?page="+pages+"&mark="+mark;
  	    }else if(maker==3){//下一页
  	      mark="n";
  	      rowKey=$("#lastRow").val();
  	      var sKey=$("#oneRow").val();
  	      pages=$("#npage").val();
  	      url="car!search.action?page="+pages+"&mark="+mark+"&rowKey="+rowKey+"&pRowKey="+sKey;
  	    }else if(maker==4){//末页
  	      pages=$("#epage").val();
  	    }
  	     document.form1.action = url;
		 document.form1.submit();
  	    
  	}

  	function init(){
  		var mtv = $("#mt").val();
  		if(mtv == 'GCXH'){
  			$("#tj").html("过车序号");
  			$("#st").attr("disabled","disabled");
  			$("#et").attr("disabled","disabled");
  			$("#div_gcxh")[0].style.display = 'block';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'CLLX'){
  			$("#tj").html("车辆类型");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'block';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'HPHM'){
  			$("#tj").html("号牌号码");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'block';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'HPZL'){
  			$("#tj").html("号牌种类");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'block';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'KKBH'){
  			$("#tj").html("卡口编号");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'block';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'HPHM_REG'){
  			$("#tj").html("号牌号码");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'block';
  		}else if(mtv == 'KKBHARR'){
  			$("#tj").html("","");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  			$("#box")[0].style.display = 'block';
  		}
  	}
  	function methodChange(){
  		var mtv = $("#mt").val();
  		if(mtv == 'GCXH'){
  			$("#tj").html("过车序号");
  			$("#st").attr("disabled","disabled");
  			$("#et").attr("disabled","disabled");
  			$("#div_gcxh")[0].style.display = 'block';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'CLLX'){
  			$("#tj").html("车辆类型");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'block';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'HPHM'){
  			$("#tj").html("号牌号码");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'block';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'HPZL'){
  			$("#tj").html("号牌种类");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'block';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'KKBH'){
  			$("#tj").html("卡口编号");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'block';
  			$("#div_hphmreg")[0].style.display = 'none';
  		}else if(mtv == 'HPHM_REG'){
  			$("#tj").html("号牌号码 :");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'block';
  		}else if(mtv == 'KKBHARR'){
  			$("#tj").html("","");
  			$("#st").attr("disabled","");
  			$("#et").attr("disabled","");
  			$("#div_gcxh")[0].style.display = 'none';
  			$("#div_cllx")[0].style.display = 'none';
  			$("#div_hphm")[0].style.display = 'none';
  			$("#div_hpzl")[0].style.display = 'none';
  			$("#div_kkbh")[0].style.display = 'none';
  			$("#div_hphmreg")[0].style.display = 'none';
  		    $("#div_kkbhArr")[0].style.display = 'block';
  			$("#box")[0].style.display = 'block';
  		}
  	}

  </script>
  </head>



  <body onload="init();">

    	 <div style="height: 100; overflow: auto;overflow-x:hidden; padding: 0px;" id="center">
    	  <s:form name="form1" action="car!search.action" method="post">
    		 <div>
	          	<table width="100%" >
	          		<tr>
	          			<td width="50">查询类型：</td>
	          			<td width="40" >
	          				<s:select id="mt" list="#{'CLLX':'按车辆类型','HPHM':'按号牌号码','HPZL':'按号牌种类','KKBH':'按卡口编号','HPHM_REG':'按号牌模糊查询','KKBHARR':'卡口编号多条件查询'}" name="methodType" theme="simple" headerKey="GCXH" headerValue="按过车序号" onchange="methodChange();"/>
	          			</td>
	          			<td width="40">时间段：</td>
	          			<td width="200">
	          				<s:textfield id="st" name="startTime" disabled="disabled" theme="simple"></s:textfield>-<s:textfield id="et" name="endTime" disabled="disabled" theme="simple"></s:textfield>
						</td>
						<td width="50"><label id="tj">过车序号</label> </td>
						<td width="160">
							<div id="div_gcxh">
							<s:textfield id="gcxh" name="gcxh" theme="simple" />
							</div>
							<div id="div_cllx" style="display: none">
							<s:textfield id="cllx" name="cllx" theme="simple" />
							</div>
							<div id="div_hphm" style="display: none">
							<s:textfield id="hphm" name="hphm" theme="simple" />
							</div>
							<div id="div_hpzl" style="display: none">
							<s:textfield id="hpzl" name="hpzl" theme="simple"/>
							</div>
							<div id="div_kkbh" style="display: none">
							<s:textfield id="kkbh" name="kkbh" theme="simple" />
							</div>
							<div id="div_hphmreg" style="display: none">
							<s:textfield id="hphmreg" name="hphmreg"  theme="simple" />
							</div>
							<div id="div_kkbhArr" >
						    <input type="hidden" value="${kkbhArrs}" id="kkbhArrs" name="kkbhArrs"/> 
							</div>

	          			<td width="30">
	          				<input type="button" value="查询" onclick="search();"/>
	          			</td>
	          		</tr>
	          		<tr>
	          			<td colspan="7">共查询数据<s:property value="count" /> 条，用时 <s:property value="time" /> 毫秒</td>

	          		</tr>
	          	</table>
	        </div>
	       
	        <div class="body" style="height:80%" >
	       		<table width="100%" height="20" class="tableborder">
	       			<tr>
	       			    <th></th>
	       			    <th>序       号</th>
	       				<th>过车编号</th>
	       				<th>卡口编号</th>
	       				<th>方向类型</th>
	       				<th>车道号</th>
	       				<th>过车时间</th>
	       				<th>号牌种类</th>
	       				<th>号牌号码</th>
	       				<th>号牌颜色</th>
	       				<th>车外廓长</th>
	       				<th>车身颜色</th>
	       				<th>车辆速度</th>
	       				<th>车辆类型</th>
	       				<th>车辆品牌</th>
	       				<th>辅助号牌种类</th>
	       				<th>辅助号牌号码</th>
	       				<th>辅助号牌颜色</th>
	       			</tr>
	          		<s:iterator value="#request.result" id="car" status="vs"> 
	          			<tr>
	          			 <td><input id="box" name="box" type="checkbox" value="${kkbh}"/></td>
	          			    <td><s:property value="#vs.index+1"/>  </td>
	          				<td><s:property value="gcxh" /></td>
	          				<td><s:property value="kkbh" /></td>
	          				<td><s:property value="fxlx" /></td>
	          				<td><s:property value="cdh" /></td>
	          				<td><s:property value="gcsj" /></td>
	          				<td><s:property value="hpzl" /></td>
	          				<td><s:property value="hphm" /></td>
	          				<td><s:property value="hpys" /></td>
	          				<td><s:property value="cwkc" /></td>
	          				<td><s:property value="clsd" /></td>
	          				<td><s:property value="csys" /></td>
	          				<td><s:property value="clpp" /></td>
	          				<td><s:property value="cllx" /></td>
	          				<td><s:property value="fzhpzl" /></td>
	          				<td><s:property value="fzhphm" /></td>
	          				<td><s:property value="fzhpys" /></td>
	          			</tr>

	          		</s:iterator>

	          	</table>

	      
	        <div  class="yang">
	                                 第  <s:property value="page"/> 页    
               <s:url id="url_pre" value="car!search.action">     
               <s:param name="page" value="page-1"></s:param></s:url>  
             <input type="hidden" value="${lastRow}" id="lastRow" name="lastRow"/> 
            <!-- <input type="hidden" value="${pRowKey}" id="pRowKey" name="pRowKey"/>  -->
             <input type="hidden" value="${oneRow}" id="oneRow" name="oneRow"/>     
	         <input type="hidden" name="spage" id="spage" value="1"/>
	         <input type="hidden" name="ppage" id="ppage" value="${page-1<=1?1:page-1}"/><!-- 上一页 -->
	         <input type="hidden" name="npage" id="npage" value="${page+1}"/><!-- 下一页 -->
	         <!--<input type="hidden" name="epage" id="epage" value="${pageCoun}"/> 末页 -->
	         <a onclick="query('1')"   >首页</a>
	         <a onclick="query('2')"   >上一页</a>
	         <a onclick="query('3')"   >下一页</a>
	         <a onclick="query('4')"   >末页</a>
            </div>  
              </div>
    	</s:form>
    	</div>
  </body>
</html>
