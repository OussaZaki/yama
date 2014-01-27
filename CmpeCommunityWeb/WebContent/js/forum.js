var Forum = {
	newTopic: function(id){
		var title = $("[name='title']").val();
		var content = $("[name='content']").val();
		$.post('/CmpeCommunityWeb/Forum/utopic/'+id, {title: title, content: content}, function(data){
			if(data['success']){
				//TODO change to created topics page
				window.location.href = '/CmpeCommunityWeb/Forum/index/'+id;
			}
			else if(data['error'] == 'need_login')
				alert("need login");
			else{
				alert("An error occured. Sorry..");
			}
		});
	}
};