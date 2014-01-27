$(document).ready(function(){
	$.post('/CmpeCommunityWeb/User/recommended', function(data){
		$("#recommendationTable tbody").html(data);
	});
});

var Posts = {
	loadNewsFeed: function(userId, element){
		element = $(element);
		$("#contentBody").load("/CmpeCommunityWeb/Profile/news/"+userId);
		element.parents().parents().find("li").removeClass('active');
		element.parents().addClass('active');
	},
	loadWall: function(userId, element){
		element = $(element);
		$("#contentBody").load("/CmpeCommunityWeb/Profile/wall/"+userId);
		element.parents().parents().find("li").removeClass('active');
		element.parents().addClass('active');
	},
	
	createWithTaggedUserId: function(targetId){
		var body = $("[name='posts']").val();
		var users = [targetId];
		$.post("/CmpeCommunityWeb/Posts/addPost", {posts: body, users: users}, function(data){
			if(data["success"])
				window.location.reload();
			else if(data["error"] == "need_login")
				window.location.href = "/CmpeCommunityWeb/";
			else
				alert("An unknown error occured, sorry for the inconvenient we may have caused.");
		});
	},
	
	createWithTaggedId: function(tagId){
		var body = $("[name='tag_post']").val();
		$.post("/CmpeCommunityWeb/Tags/addPost/"+tagId, {body: body}, function(data){
			if(data["success"])
				window.location.reload();
			else if(data["error"] == "need_login")
				window.location.href = "/CmpeCommunityWeb/";
			else
				alert("An unknown error occured, sorry for the inconvenient we may have caused.");
		});
	}
};

var Reply = {
	create: function(postId){
		var body = $("[name='reply_"+postId+"']").val();
		$.post("/CmpeCommunityWeb/Posts/reply/"+postId, {body: body}, function(data){
			if(data["success"])
				window.location.reload();
			else if(data["error"] == "need_login")
				window.location.href = "/CmpeCommunityWeb/";
			else
				alert("An unknown error occured, sorry for the inconvenient we may have caused.");
		});
		return false;
	}
};

var Tags = {
		loadUsers: function(tagId){
			$("#contentBody").load("/CmpeCommunityWeb/Tags/users/"+tagId);
			console.log("loading news feed");
		},
		loadWall: function(tagId){
			$("#contentBody").load("/CmpeCommunityWeb/Tags/wall/"+tagId);
			console.log("loading wall");
		},
		addTag: function(userId){
			$("#contentBody").load("/CmpeCommunityWeb/Tags/addTags/"+userId);
			$("#contentBody").load("/CmpeCommunityWeb/Tags/wall/16");
			console.log("loading wall");
		},
		
		add: function(userId){
			var tags = $("[name='hidden-tags']").val();
			$.post("/CmpeCommunityWeb/Tags/addTags/"+userId, {tags: tags}, function(data){
				if(data["success"])
					window.location.reload();
				else if(data["error"] == "need_login")
					window.location.href = "/CmpeCommunityWeb/";
				else
					alert("An unknown error occured, sorry for the inconvenient we may have caused.");
			});
		},
		removeTag: function(tag, tagId){
			  $.post('/CmpeCommunityWeb/Tags/removeTag/'+ tagId,{}, function(data){
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
		$("#contentBody").load("/CmpeCommunityWeb/Surveys/loadMySurveys/"+userId);
		console.log("load my survey");
	},
	loadCompletedSurveys: function(userId){
		$("#contentBody").load("/CmpeCommunityWeb/Surveys/loadCompletedSurveys/"+userId);
		console.log("load completed survey");
	},
	add: function(userId){
		var choices =$("[name^=choice]").map(function(){return $(this).val();}).get();
		var question=$("[name=newquestion]").val();
		//$.post("/CmpeCommunityWeb/Surveys/addSurvey/"+userId,{'question':question,'choices':choices},function(data){
		$.ajax({
			  type: "POST",
			  url: "/CmpeCommunityWeb/Surveys/addSurvey/"+userId,
			  data: 'question='+question+'&choices='+choices,
			  datatype: "json",
			  success: function(data){	
				  if(data["success"])
					  window.location.reload();
				  else if(data["error"] == "need_login")
					  window.location.href = "/CmpeCommunityWeb/";
				  else
				alert("An unknown error occured, sorry for the inconvenient we may have caused.");
			  }  
		});
		console.log("adding survey");
	},
	submit: function(surveyId){
		var choiceId = $("[name='survey"+surveyId+"']:checked").val();
		$.post('/CmpeCommunityWeb/Surveys/vote/'+surveyId+"/"+choiceId, function(data){
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
			$("#contentBody").load("/CmpeCommunityWeb/Events/myEvents/"+userId);
		},
		
		loadAttendedEvents: function(userId){
			$("#contentBody").load("/CmpeCommunityWeb/Events/attendedEvents/"+userId);
		},
		add: function(){
			var place =$("[name='place']").val();
			var dateTime=$("[name='datetime']").val();
			var description=$("[name='description']").val();
			$.post('/CmpeCommunityWeb/Events/addEvents', {place: place, datetime: dateTime, description: description}, function(data){
				  if(data["success"])
					  Events.loadMyEvents();
				  else if(data["error"] == "need_login")
					  window.location.href = "/CmpeCommunityWeb/";
				  else
					  alert("An unknown error occured, sorry for the inconvenient we may have caused.");
			});
		},
		attend: function(id){
			$.post('/CmpeCommunityWeb/Events/attend/'+id, function(data){
				console.log(data);
				if(data['success'] || data['error'] == 'need_login')
					window.location.reload();
				else
					alert("An unknown error occured, sorry for the inconvenient we may have caused.");
			});
		},
		unAttend: function(id){
			$.post('/CmpeCommunityWeb/Events/unAttend/'+id, function(data){
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
			$.post('/CmpeCommunityWeb/Profile/update/'+userId, {name: name, password: password1, email: email}, function(data){
				 window.location.reload();
			});
		}
		
};
