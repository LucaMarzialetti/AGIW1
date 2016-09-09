<!--Author: W3layouts
Author URL: http://w3layouts.com
License: Creative Commons Attribution 3.0 Unported
License URL: http://creativecommons.org/licenses/by/3.0/
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.json.JSONArray"%>
<!DOCTYPE HTML>
<html>
<head>
<title>Bomba Retrieval - Results</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="icon" href="/images/bomb_favicon_16.png" type="image/png" />
<link rel="icon" href="/images/bomb_favicon_32.png" type="image/png" />
<link rel="icon" href="/images/bomb_favicon_48.png" type="image/png" />
<link rel="icon" href="images/bomb_favicon_64.png" type="image/png" />
<link rel="icon" href="/images/bomb_favicon_16.ico" type="image/ico" />
<link rel="icon" href="/images/bomb_favicon_32.ico" type="image/ico" />
<link rel="icon" href="/images/bomb_favicon_48.ico" type="image/ico" />
<link rel="icon" href="images/bomb_favicon_64.ico" type="image/ico" />
<!-- Custom Theme files -->
<link href="css/style.css" rel="stylesheet" type="text/css" media="all" />

<!--Google Fonts-->
<link
	href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800'
	rel='stylesheet' type='text/css'>
<!--Google Fonts-->
</head>
<body>
	<!--search start here-->
	<div class="search2">
		<i> </i>
		<div class="s-bar">
			<form action="processaQuery" method="get">
				<input type="text" name="query"
					value="<%=session.getAttribute("originalQuery")%>"
					onfocus="this.value = '';"
					onblur="if (this.value == '') {this.value = '<%=session.getAttribute("originalQuery")%>';}">
				<input type="submit" value="Go" />
			</form>
		</div>
		<p>
			Totale risultati trovati: <a href="#"><%=session.getAttribute("total")%></a>
		</p>
	</div>
	<div class="clear"></div>
	<!--search end here-->
	<div class="clear"></div>
	<div class="wrap">
		<div class="vdo-log">
			<div class="heading">
				<h2>
					Risultati per:
					<%=session.getAttribute("originalQuery")%>
				</h2>
			</div>
			<%
				JSONArray list = (JSONArray) session.getAttribute("result");
				System.out.println("[WEBINTERFACE]: response to query '" + session.getAttribute("originalQuery") + "' with "+list.length()+" result/s");
				for (int k = 0; k < list.length(); k++) {
			%>
			<div class="vdo-list">
				<div class="vdo-info">
					<div class="vdo-title">
						<a
							href=<%=list.getJSONObject(k).getJSONObject("source").getString("url")%>><h3>
								<%
									out.print(list.getJSONObject(k).getJSONObject("source").getString("title"));
								%>
							</h3></a>
					</div>
					<div class="vdo-detail">
						<p>
							<a
								href=<%=list.getJSONObject(k).getJSONObject("source").getString("url")%>
								target="_blank"> 
							<%
							 	out.print(list.getJSONObject(k).getJSONObject("source").getString("url"));
							 %>
							</a>
						</p>
					</div>
					<br />
					<div class="vdo-desc">
						<%
							out.print(list.getJSONObject(k).getJSONObject("source").getString("description"));
						%>
					</div>
				</div>
				<div class="clear"></div>
			</div>
			<%
				}
			%>
			<div class="clear"></div>
			
<!-- 			 <div class="page-nav"> -->
<!-- 				<ul> -->
<!-- 					<li><a href="#">&laquo; Prev</a></li> -->
<!-- 					<li><span class="current">1</span></li> -->
<!-- 					<li><a href="#">2</a></li> -->
<!-- 					<li><a href="#">3</a></li> -->
<!-- 					<li><a href="#">Next &raquo;</a></li> -->
<!-- 				</ul> -->
<!-- 			</div>  -->
		</div>
	</div>
	<div class="clear"></div>
</body>
</html>