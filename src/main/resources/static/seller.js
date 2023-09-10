    $(document).ready(function() {
        debugger;
        });
         function ajaxPost() {
                debugger;
                var Name = $("#txtSellerName").val();
                var EmailId = $("#txtSellerEmail").val();
                var Address = $("#txtSellerAddress").val();
                var MobNo = $("#txtSellerNumber").val();
                if(Name!='' && EmailId!='' && Address!='' && MobNo!='' ){
                 var formData = {
                                       name:Name,
                                       emailId:EmailId,
                                       address:Address,
                                       mobNo:MobNo
                                    }
                }
                else{
                alert("Please enter valid input");
                return;
                }

                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : "/seller/add",
                        data : JSON.stringify(formData),
                        dataType : 'json',
                        success : function(result) {
                            debugger;
                           /* if (result.status == "success") {

                                $("#postResultDiv").html(
                                        "" + result.data.bookName
                                                + "Post Successfully! <br>"
                                                + "---> Congrats !!" + "</p>");
                            } else {
                                $("#postResultDiv").html("<strong>Error</strong>");
                            }*/
                            console.log(result);
                            alert("data insert successfully");
                        },
                        error : function(e) {
                            debugger;
                            if(e.status==400){
                              alert("Duplicate Email Or Mobile number");
                            }
                            console.log("ERROR: ", e);
                        }
                    });
                }