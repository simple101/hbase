
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
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  <script type="text/javascript">
  
  	function search(){
  		alert("search");
  		document.form1.action = "hbase!search.action";
		document.form1.submit();
  	}
  
  </script>
  
  <body>
  
    	 <div style="height: 100; overflow: auto;overflow-x:hidden; padding: 0px;" id="center"> 
    	  <s:form name="form1" action="#" method="post">
    		 <div>
	          	<table>
	          		<tr>
	          			<td>手机号码：</td>
	          			<td>
	          				<s:textfield id="pn" name="phoneNum" theme="simple"></s:textfield>
						</td>
	          			<td>
	          				<input type="button" value="查询" onclick="search();"/>
	          			</td>
	          		</tr>
	          	
	          	</table>
	        </div>
	       </s:form>
	        <div class="body" style="height:100%">
	        
	       		<table>
	          		<s:iterator value="#request.result" id="r">
	          			<tr>
	          				<td><s:property value="r"/></td>
	          			</tr>
	          		
	          		</s:iterator>
	          	
	          	</table>
	        
	        </div>
	        
	        
    	</div>

  </body>
</html>
