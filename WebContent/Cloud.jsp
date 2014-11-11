<%@page import="Tables.TagsTable"%>
<%@page import="drivers.TagsDriver"%>
<%@page import="drivers.SearchDriver"%>
<%@page import="drivers.ForumsDriver"%>
<%@page import="Tables.UserTable"%>
<%@page import="Tables.ForumPostTable"%>
<%@page import="com.sun.org.apache.xpath.internal.FoundIndex"%>
<%@page import="Tables.ForumTopicTable"%>
<%@page import="java.util.Map"%>
<%@page import="Tables.ForumsTable"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>YAMA - You Ask, Moroccans Answer Forums</title>
<link rel="stylesheet" href="/YamaWeb/css/bootstrap.min.css">
<style>

.center-text{
	text-align: center !important;
}
	a.red { color: #f00 }
	a.green { color: #0c0 }
	a.purple { color: #f09 }
	a.blue {color: #33B5E5}
	a.huge { font-family: Impact,sans-serif; font-size: 40px }
	a.large { font-family: 'Arial Black',sans-serif; font-size: 32px }
	a.medium { font-family: Verdana,sans-serif; font-size: 28px }
	a.small { font-family: Georgia,sans-serif; font-size: 22px }
</style>
  <link rel="stylesheet" href="http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.8.3.js"></script>
    <script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></script>
    <style>
    	.nav-tabs > li > a {
    border: 1px solid transparent;
    border-radius: 4px 4px 0 0;
    cursor: pointer;
    line-height: 20px;
    padding-bottom: 8px;
    padding-top: 8px;
	}
	.nav-tabs > .active > a, .nav-tabs > .active > a:hover {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    background-color: #FFFFFF;
    border-color: #DDDDDD #DDDDDD transparent;
    border-image: none;
    border-style: solid;
    border-width: 1px;
    color: #555555;
    cursor: pointer;
}
.item-category {
  font-weight:italic;
  color:#dd6600;
  float:right;
} 
    .ui-autocomplete-category {
        font-weight: bold;
        padding: .2em .4em;
        margin: .8em 0 .2em;
        line-height: 1.5;
    }
    input.span3, textarea.span3, .uneditable-input.span3 {
    width: 290px;
    }
    </style>
    <script>
    $.widget( "custom.catcomplete", $.ui.autocomplete, {
    	  _renderMenu: function( ul, items ) {
    	   var self = this,
    	    currentCategory = "";
    	   $.each( items, function( index, item ) {
    		   if (item.category != currentCategory) {
    	     //ul.append( "<i>" + item.category + "</i>" );
    	     currentCategory = item.category;
    		   }
    	    self._renderItem( ul, item );
    	   });
    	  }
    	 });
    $(function() {
        var data = [
            <%=SearchDriver.getSearchAutoComplete()%>
            { value: "",label: " ", category: " " }
        ];
 
        $( "#search" ).catcomplete({
            delay: 0,
            source: data,
            select: function(event,ui) {
            	window.location.href=ui.item.value;
            	$("#search").val(ui.item.label);
            	return false;
            }
        }).data("catcomplete")._renderItem = function (ul, item) {
        	var MyHtml = "<a>" + "<div class='ac' >" +
            "<div>" +
            "<span>" + item.label + "</span>" +
            "<span class='item-category'><i>" + item.category + "</i></span>" +
            "</div>" +
            "</div>" + "</a>";
return $("<li></li>").data("item.autocomplete", item).append(MyHtml).appendTo(ul);
};
    });
    </script>
    <script src="/YamaWeb/js/tagcanvas.min.js" type="text/javascript"></script>
    
</head>
<body>

<div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container">
                <a href="/YamaWeb/Profile/cloud" class="brand">YAMA</a>
                <div class="nav-collapse">
                    <ul class="nav">
                        <li><a href="/YamaWeb/Profile">Home</a></li>
                        <li><a href="/YamaWeb/Forum">Forum</a></li>
                    </ul>

                    <form action="#" class="navbar-search">
                        <input id="search" type="text" placeholder="Search" class="search-query span3"
                            name="q" value="" /> <input type="hidden" name="scope"
                            id="autocomplete" value="posts">
                    </form>
                    <ul class="nav pull-right">
                        <li><a href="/YamaWeb/Profile/edit">Account</a></li>
                        <li><a href="/YamaWeb/User/logout">Logout</a></li>
                    </ul>
                </div>
                
            </div>
        </div>
    </div>

	<div class="container">
		<div class="span10">

				<div id="myCanvasContainer">
					<canvas width="900" height="700" id="tagcanvas">
				        <p>Anything in here will be replaced on browsers that support the canvas element</p>
				    </canvas>
				</div>

				<div id="taglist" style="display: none">
					<ul>
					<%
					TagsTable[] tags=TagsDriver.getRecentTags(40);
					String[] colors = {"red", "green", "purple", "blue"};
					String[] sizes = {"small", "medium", "large", "huge"};
					for(int i = 0; i < tags.length; i++){ %>
						<li><a class="<%= colors[i %colors.length]%> <%= sizes[i %sizes.length]%>" 
						href="/YamaWeb/Tags/details/<%=tags[i].getId() %>"><%= tags[i].getTag() %></a></li>
						
					<%} %>
					</ul>
				</div>
			</div>
	
	</div>

	<script type="text/javascript" src="/YamaWeb/js/bootstrap.js"></script>
		<script>
		window.onload = function() {
			TagCanvas.textFont = 'Impact,"Arial Black",sans-serif';
			TagCanvas.textColour = '#00f';
			TagCanvas.textHeight = 25;
			TagCanvas.outlineColour = '#f60';
			TagCanvas.outlineThickness = 5;
			TagCanvas.outlineOffset = 1;
			TagCanvas.outlineMethod = 'block';
			TagCanvas.maxSpeed = 0.06;
			TagCanvas.minBrightness = 0.5;
			TagCanvas.depth = 0.75;
			TagCanvas.pulsateTo = 0.2;
			TagCanvas.pulsateTime = 0.75;
			TagCanvas.decel = 0.9;
			TagCanvas.reverse = true;
			TagCanvas.hideTags = false;
			TagCanvas.shadow = '#ccf';
			TagCanvas.shadowBlur = 3;
			TagCanvas.wheelZoom = false;
			try {
				TagCanvas.Start('tagcanvas','taglist', {textFont:null, textColour:null, weight: true});
			} catch(e) {
		        document.getElementById('tagcanvas').style.display = 'none';
			}
		};
	</script>
</body>
</html>