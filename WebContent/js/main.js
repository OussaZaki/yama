$(document).ready(function(){
	$.post('/YamaWeb/User/recommended', function(data){
		$("#recommendationTable tbody").html(data);
	});
});

var Posts = {
	loadNewsFeed: function(userId, element){
		element = $(element);
		$("#contentBody").load("/YamaWeb/Profile/news/"+userId);
		element.parents().parents().find("li").removeClass('active');
		element.parents().addClass('active');
	},
	loadWall: function(userId, element){
		element = $(element);
		$("#contentBody").load("/YamaWeb/Profile/wall/"+userId);
		element.parents().parents().find("li").removeClass('active');
		element.parents().addClass('active');
	},
	
	createWithTaggedUserId: function(targetId){
		var body = $("[name='posts']").val();
		var users = [targetId];
		$.post("/YamaWeb/Posts/addPost", {posts: body, users: users}, function(data){
			if(data["success"])
				window.location.reload();
			else if(data["error"] == "need_login")
				window.location.href = "/YamaWeb/";
			else
				alert("An unknown error occured, sorry for the inconvenient we may have caused.");
		});
	},
	
	createWithTaggedId: function(tagId){
		var body = $("[name='tag_post']").val();
		$.post("/YamaWeb/Tags/addPost/"+tagId, {body: body}, function(data){
			if(data["success"])
				window.location.reload();
			else if(data["error"] == "need_login")
				window.location.href = "/YamaWeb/";
			else
				alert("An unknown error occured, sorry for the inconvenient we may have caused.");
		});
	}
};

var Reply = {
	create: function(postId){
		var body = $("[name='reply_"+postId+"']").val();
		$.post("/YamaWeb/Posts/reply/"+postId, {body: body}, function(data){
			if(data["success"])
				window.location.reload();
			else if(data["error"] == "need_login")
				window.location.href = "/YamaWeb/";
			else
				alert("An unknown error occured, sorry for the inconvenient we may have caused.");
		});
		return false;
	}
};

var Tags = {
		loadUsers: function(tagId){
			$("#contentBody").load("/YamaWeb/Tags/users/"+tagId);
			console.log("loading news feed");
		},
		loadWall: function(tagId){
			$("#contentBody").load("/YamaWeb/Tags/wall/"+tagId);
			console.log("loading wall");
		},
		addTag: function(userId){
			$("#contentBody").load("/YamaWeb/Tags/addTags/"+userId);
			$("#contentBody").load("/YamaWeb/Tags/wall/16");
			console.log("loading wall");
		},
		
		add: function(userId){
			var tags = $("[name='hidden-tags']").val();
			$.post("/YamaWeb/Tags/addTags/"+userId, {tags: tags}, function(data){
				if(data["success"])
					window.location.reload();
				else if(data["error"] == "need_login")
					window.location.href = "/YamaWeb/";
				else
					alert("An unknown error occured, sorry for the inconvenient we may have caused.");
			});
		},
		removeTag: function(tag, tagId){
			  $.post('/YamaWeb/Tags/removeTag/'+ tagId,{}, function(data){
					if(data["success"]){
						$(tag).closest('span').remove();
						$('#successMessage').show();
					}else{
						$('#errorMessage').show();	
					}	
				});
		  }
};
var Surveys = {
	loadMySurveys: function(userId){
		$("#contentBody").load("/YamaWeb/Surveys/loadMySurveys/"+userId);
		console.log("load my survey");
	},
	loadCompletedSurveys: function(userId){
		$("#contentBody").load("/YamaWeb/Surveys/loadCompletedSurveys/"+userId);
		console.log("load completed survey");
	},
	add: function(userId){
		var choices =$("[name^=choice]").map(function(){return $(this).val();}).get();
		var question=$("[name=newquestion]").val();
		//$.post("/YamaWeb/Surveys/addSurvey/"+userId,{'question':question,'choices':choices},function(data){
		$.ajax({
			  type: "POST",
			  url: "/YamaWeb/Surveys/addSurvey/"+userId,
			  data: 'question='+question+'&choices='+choices,
			  datatype: "json",
			  success: function(data){	
				  if(data["success"])
					  window.location.reload();
				  else if(data["error"] == "need_login")
					  window.location.href = "/YamaWeb/";
				  else
				alert("An unknown error occured, sorry for the inconvenient we may have caused.");
			  }  
		});
		console.log("adding survey");
	},
	submit: function(surveyId){
		var choiceId = $("[name='survey"+surveyId+"']:checked").val();
		$.post('/YamaWeb/Surveys/vote/'+surveyId+"/"+choiceId, function(data){
			if(data["success"]){
				var html = "";
				for(var i in data.choices){
					html = html + '<div class="row-fluid"><div class="progress progress-warning span2">';
					html = html + '<div class="bar" style="width: '+data.choices[i].percentage+'%;">';
					html = html + '<p>&nbsp;'+data.choices[i].vote+'</p>';
					html = html + '</div></div><div class="span5">';
					html = html + '<p>'+data.choices[i].choice+'</p>';
					html = html + '</div>';
					html = html + '</div>';
				}
				console.log(html);
				$("#surveyContainer"+surveyId).html(html);
			}
			else if(data["error"] == "need_login")
				window.location.reload();
			else
				alert("An unknown error occured, sorry for the inconvenient we may have caused.");
		});
	}
};



var Events = {
		loadMyEvents: function(userId){
			if(!userId)
				userId = 0;
			$("#contentBody").load("/YamaWeb/Events/myEvents/"+userId);
		},
		
		loadAttendedEvents: function(userId){
			$("#contentBody").load("/YamaWeb/Events/attendedEvents/"+userId);
		},
		add: function(){
			var place =$("[name='place']").val();
			var dateTime=$("[name='datetime']").val();
			var description=$("[name='description']").val();
			$.post('/YamaWeb/Events/addEvents', {place: place, datetime: dateTime, description: description}, function(data){
				  if(data["success"])
					  Events.loadMyEvents();
				  else if(data["error"] == "need_login")
					  window.location.href = "/YamaWeb/";
				  else
					  alert("An unknown error occured, sorry for the inconvenient we may have caused.");
			});
		},
		attend: function(id){
			$.post('/YamaWeb/Events/attend/'+id, function(data){
				console.log(data);
				if(data['success'] || data['error'] == 'need_login')
					window.location.reload();
				else
					alert("An unknown error occured, sorry for the inconvenient we may have caused.");
			});
		},
		unAttend: function(id){
			$.post('/YamaWeb/Events/unAttend/'+id, function(data){
				if(data['success'])
					window.location.reload();
				else
					alert("An unknown error occured, sorry for the inconvenient we may have caused.");
			});
		}
};
var Profile = {
		update: function(userId){
			var name =$("[name='name']").val();
			var password1=$("[name='password_signup']").val();
			var password2=$("[name='re-password']").val();
			var email=$("[name='email_signup']").val();
			if (password1!=password2) {
				alert("Passwords are inconsistent");
				return;
			}
			$.post('/YamaWeb/Profile/update/'+userId, {name: name, password: password1, email: email}, function(data){
				 window.location.reload();
			});
		}
		
};
