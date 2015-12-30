
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
    
    <title>车查询</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<style type="text/css">
		#menu { 
			font:12px verdana, arial, sans-serif; /* 设置文字大小和字体样式 */
		}
		#menu, #menu li {
			list-style:none; /* 将默认的列表符号去掉 */
			padding:0; /* 将默认的内边距去掉 */
			margin-right:15; /* 将默认的外边距去掉 */
			float:left; /* 往左浮动 */
		}
		#menu li a {
			display:block; /* 将链接设为块级元素 */
			padding:5px 30px; /* 设置内边距 */
			background:#3A4953; /* 设置背景色 */
			color:#fff; /* 设置文字颜色 */
			text-decoration:none; /* 去掉下划线 */
			border-right:1px solid #000; /* 在左侧加上分隔线 */
		}
</style>
  </head>
  <body>
  
    	 <div style="height: 40; overflow: auto;overflow-x:hidden; padding: 0px 0px 0px 0px;" id="center"> 
    	 	<ul id="menu" >
    	 			<li>
						<a  href="<%=path%>/jsp/CarQuery.jsp" target="content">车辆查询</a>
					</li>
					<li>
						<a  href="<%=path%>/jsp/HDQuery.jsp" target="content">话单查询</a>
					</li>
	        </ul>
    	</div>
	<iframe  name="content" src="<%=path%>/jsp/CarQuery.jsp"  frameborder="0" id="center" width="100%" height="100%" align="top"></iframe>
  		
</body>
  </body>
</html>
