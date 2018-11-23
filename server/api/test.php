<?php

if(isset($_GET["action"])){
	$action = $_GET['action'];
}

if($action == allow){
	$randVal = mt_rand(0,1);

	if($randVal == 0){
	   $allow = false;
	}else{
	   $allow = true;
	}
	
	print(json_encode($allow));
}

?>