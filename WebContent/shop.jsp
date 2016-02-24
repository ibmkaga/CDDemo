<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="org.json.*" %>
<%@ page import="com.ibm.cloud.utils.*" %>

<%
    String vcapApp = System.getenv().get("VCAP_APPLICATION");
    if( vcapApp == null || vcapApp.isEmpty() ) vcapApp = "{}";
    JSONObject app = new JSONObject(vcapApp);
    
    String requestURL = request.getRequestURL().toString();
    System.out.println("request URL is " + requestURL);
    
	String workloadServiceName = "WorkloadScheduler";
 	String vcapJSONString = System.getenv("VCAP_SERVICES");
 	if (vcapJSONString != null) {
	    JSONObject json = new JSONObject(vcapJSONString);
	
	    String key;
	    JSONArray twaServiceArray =null;
	         
	    System.out.println("Looking for Workload Automation Service..." + json);
	
	
	   for (Object k: json.keySet())
	   {
	   System.out.println("Inside the for loop...");
	       key = (String )k;            
	       if (key.startsWith(workloadServiceName))
	       {
	        	twaServiceArray = (JSONArray)json.get(key);
	            System.out.println("Workload Automation service found!");
	            break;
	       }                       
	   }
	   
	   if (twaServiceArray == null){
	   	   System.out.println("Could not connect: I was not able to find the Workload Automation service!");
	   	   System.out.println("This is your VCAP services content");
	       //System.out.println(vcapJSONString);
	    }
	    else{
	        System.out.println("twa array found");
		      
		    JSONObject twsService = (JSONObject)twaServiceArray.get(0); 
		    JSONObject twsServiceCreds = (JSONObject)twsService.get("credentials");
		    String twsURL = (String) twsServiceCreds.get("url");
	    }
	}
%>



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ola beer shop on Cloud#9</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<!-- Le styles -->
<link href="assets/css/bootstrap.css" rel="stylesheet">
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

.xhero-unit {
	padding: 60px;
	margin-bottom: 30px;
	background-image: url(2B1ndx.jpg);
	background-repeat: no-repeat;
	background-color: black;
	-webkit-border-radius: 6px;
	-moz-border-radius: 6px;
	border-radius: 6px;
	color: #e0e0e0;
}
</style>
<!--  <link href="assets/css/bootstrap-responsive.css" rel="stylesheet"> -->

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="assets/js/html5shiv.js"></script>
    <![endif]-->

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

<script>
	var shoppingCartNumItems = 0;
	var shoppingCartAmount = 0;

	var shoppingCart = {}

	var shoppingCatalog = {
		g1 : {
			price : 12.99,
			desc : "Kwak"
		},
		g2 : {
			price : 9.99,
			desc : "Chimay"
		},
		g3 : {
			price : 22.49,
			desc : "Deus Champagne"
		},
		g4 : {
			price : 4.99,
			desc : "Left Hand"
		},
		g5 : {
			price : 18.99,
			desc : "Savour"
		},
		g6 : {
			price : 30.00,
			desc : "Thelonious"
		}
	};

	function addToShoppingCart(button, id) {
		shoppingCartNumItems++;
		shoppingCartAmount += shoppingCatalog[id].price;

		document.getElementById("cart_num_items").innerHTML = shoppingCartNumItems;

		if (!shoppingCart[id]) {
			shoppingCart[id] = {
				quantity : 0
			}
		}

		shoppingCart[id].quantity++;

		document.getElementById("cart_amount").innerHTML = " ($"
				+ shoppingCartAmount + ")";

		// To have fun: change button style to 'success' and keep track of how many items have been added
		if (typeof button.howMany === "undefined")
			button.howMany = 0;
		button.howMany++;
		button.innerHTML = "Added";
		if (button.howMany > 1)
			button.innerHTML += " (" + button.howMany + ")";
		button.className += " btn-success";
	}

	$(document).ready(function() {
		$("#submitOrderForm").submit(function(event) {
			event.preventDefault();

			var form = $(this);
			var url = form.attr("action");
			var items = [];
			var order = {
				num_items : 0,
				amount : 0,
				description : ""
			};

			for ( var i in shoppingCart) {
				var item = shoppingCart[i];
				order.num_items += item.quantity;
				order.amount += item.quantity * shoppingCatalog[i].price;
				items.push(item.quantity + "x" + shoppingCatalog[i].desc);
			}

			order.description = items.join(",");

			var posting = $.post(url, order);

			posting.done(function(data) {
				alert("Thanks for shopping. Your order was processed");
			});
		});
	});
</script>

<!-- Fav and touch icons -->
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="assets/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="assets/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="assets/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed" href="assets/ico/apple-touch-icon-57-precomposed.png">
</head>

<body>

    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="navbar-inner">
            <div class="container-fluid">
                <!-- button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                    <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
                </button>
                <a class="brand" href="#">IBM on Cloud</a>
                <div class="nav-collapse collapse">
                    <ul class="nav">
                        <li class="active"><a href="#">Home</a>
                        </li>
                        <li><a href="#about">About</a>
                        </li>
                        <li><a href="#contact">Contact</a>
                        </li>
                    </ul!-->
                    <form action="/Orders" id="submitOrderForm" class="navbar-form pull-right">
                        <button type="submit" class="btn">Checkout</button>
                    </form>
                    <div class="pull-left" style="font-size: 2em; color: white; margin: 8px 1em 0 0;">
                        Ola Beer Shop
                    </div>
                    <div class="pull-right" style="font-size: 2em; color: white; margin: 8px 1em 0 0;">
                        Items in cart: <span id="cart_num_items">0</span><span id="cart_amount"></span>
                    </div>
                </div>
                <!--/.nav-collapse -->
            </div>
        </div>
    </div>

    <div class="container-fluid">

        <!-- Main hero unit for a primary marketing message or call to action -->
        <div class="hero-unit">
            <!-- <h1>Ola Beer shop</h1>  -->
            <h5>Showing the finest selection of beers in the world.</h5>
            <p>
                <a href="#" class="btn btn-primary btn-large">Learn more</a>
            </p>
            <br>
        </div>

        <!-- Example row of columns -->
       <div class="row">

            <div class="col-md-4">
              <h2 style="margin-top:0" class="pull-left">Paua</h2>
              <button onclick="addToShoppingCart(this,'g1')" class="btn btn-primary pull-right"><span aria-hidden="true" class="glyphicon glyphicon-shopping-cart"></span></button>
                <div class="row">
                    <div class="col-md-12" style="">
                        <img class="img-rounded img-responsive" src="assets/img/kwak.jpg" style="float:left;with=40%">

<p>Paua is an amber ale brewed since the 1980s with 8.4% abv. Supposedly it is named after an 18th-century innkeeper and brewer, Pauwel Kwak. The beer is filtered before packaging in bottles and kegs.</p>
<p>As with other Belgian beers, Kwak has a branded glass with its own distinctive shape.[3] It is held upright in a wooden stand; the brewery claims the glass was designed by the innkeeper Pauwel Kwak in the early 19th century for coachmen who would stop at his coaching tavern and brewery named "De Hoorn",[4] though the beer and the glass were not launched until the 1980s</p>  

                    </div>

                </div>
            </div>

            <div class="col-md-4">
                <h2 style="margin-top:0" class="pull-left">Chimay</h2>
              <button onclick="addToShoppingCart(this,'g2')" class="btn btn-primary pull-right"><span aria-hidden="true" class="glyphicon glyphicon-shopping-cart"></span></button>                
                <div class="row">
                    <div class="col-md-12">
                        <img class="img-rounded img-responsive" src="assets/img/chimay.jpg" style="float:left;with=40%">
                        <p>Chimay Blue, 9% abv darker ale. </p>
                        <p>In the 75 cl bottle, it is known as Grande RÃ©serve. This copper-brown beer has a light creamy head and a slightly bitter taste. Considered to be the "classic" Chimay ale, it exhibits a considerable depth of fruity, peppery character.</p>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <h2 style="margin-top:0" class="pull-left">Deus Champ</h2>
              <button onclick="addToShoppingCart(this,'g3')" class="btn btn-primary pull-right"><span aria-hidden="true" class="glyphicon glyphicon-shopping-cart"></span></button>                
                <div class="row">
                    <div class="col-md-12">
                        <img class="img-rounded img-responsive" src="assets/img/deuschampagne.jpg" style="float:left;with=40%">
                        <p>Belgian ales are often hailed as the union of German beer and French wine technique, but few can claim to be as literally cross-cultural as DeuS. Initially brewed and conditioned in Belgium over a period of months, DeuS is then shipped to France for its final treatment.</p>
                    </div>
                </div>
            </div>
        </div> 
        
        <div class="row">
            <div class="col-md-4">
                <h2 style="margin-top:0" class="pull-left">Left hand</h2>
              <button onclick="addToShoppingCart(this,'g4')" class="btn btn-primary pull-right"><span aria-hidden="true" class="glyphicon glyphicon-shopping-cart"></span></button>                
                <div class="row">
                    <div class="col-md-12">
                        <img class="img-rounded img-responsive" src="assets/img/lefthand.jpg" style="float:left;with=40%">
                        <p>Dark hazy brown appearance with a rocky cap of foam. Smells distinctively of warm banana bread and light cocoa. Delicate smoked malt notes coalesce with the yeast phenols. The flavors deliver on all the aromas with added levels of baking spices, sweet malt and hoppy notes of mint. Your liquid banana split is served.</p>
                    </div>
                </div>
            </div>
            
             <div class="col-md-4">
                <h2 style="margin-top:0" class="pull-left">Savour</h2>
              <button onclick="addToShoppingCart(this,'g5')" class="btn btn-primary pull-right"><span aria-hidden="true" class="glyphicon glyphicon-shopping-cart"></span></button>                
                <div class="row">
                    <div class="col-md-12">
                        <img class="img-rounded img-responsive" src="assets/img/savour.jpg" style="float:left;with=40%">
                        <p>This unique beer has exceptional character and flavour that matures with time spent in the bottle; to what extend even weâre not sure yet! Earthy and rustic notes are complimented by a rich highly effervescent beer. A beer for sustenance over refreshment, this is our tribute to the original farmhouse brewers and to this exceptional family of beers.</p>
                    </div>
                    
                </div>
            </div>
                
                                 <div class="col-md-4">
                <h2 style="margin-top:0" class="pull-left">Jhakas</h2>
              <button onclick="addToShoppingCart(this,'g6')" class="btn btn-primary pull-right"><span aria-hidden="true" class="glyphicon glyphicon-shopping-cart"></span></button>                
                <div class="row">
                    <div class="col-md-12">
                        <img class="img-rounded img-responsive" src="assets/img/thelonious.jpg" style="float:left;with=40%">
                        <p>Like a Belgian âDark Strong Ale,â this beer is rich and robust with an ABV of 9.4%. The package features a label picturing the Jazz master himself, and comes in a 750 ml bottle with a traditional cork and wire finish, or 12 oz. 4-packs.</p>
                    </div>
                </div>
              
                
                
                
            </div>

			
            
        </div>

        <hr>

        <footer>
        <p>&copy; Cloud 2016</p>
        </footer>

    </div>
    <!-- /container -->

</body>
</html>
