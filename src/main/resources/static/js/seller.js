    $(document).ready(function() {
        console.log("js call");
        });

        function BindProductList(category){
         debugger;

            $.ajax({
                   type : "POST",
                   contentType : "application/json",
                   url : "/product/get-products-by-category",
                   data : JSON.stringify({productCategory:category}),
                   dataType : 'json',
                   success : function(result) {
                       debugger;

                       console.log(result);
                       alert("product insert successfully");
                   },
                   error : function(e) {
                       debugger;
                       if(e.status==400){
                         alert("error in product");
                       }
                       console.log("ERROR: ", e);
                   }
            });
         }

         function ajaxPost() {
                debugger;

                 var formData = {
                                     name: "Yogesh",
                                     quantity:"2",
                                     price: "100000000",
                                     productCategory : "FOOTWEAR"
                                }

                    $.ajax({
                        type : "POST",
                        contentType : "application/json",
                        url : "/product/add",
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
                            alert("product insert successfully");
                        },
                        error : function(e) {
                            debugger;
                            if(e.status==400){
                              alert("error in product");
                            }
                            console.log("ERROR: ", e);
                        }
                    });
                }