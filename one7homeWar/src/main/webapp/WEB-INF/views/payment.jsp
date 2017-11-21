<html>
<head>
	<title>Mmerchant checkout page</title>
</head>
<body>
<%-- 	<%
	 String accessCode= "";		//Put in the Access Code in quotes provided by CCAVENUES.
	 String workingKey = "";    //Put in the 32 Bit Working Key provided by CCAVENUES.  
	 Enumeration enumeration=request.getParameterNames();
	 String ccaRequest="", pname="", pvalue="";
	 while(enumeration.hasMoreElements()) {
	      pname = ""+enumeration.nextElement();
	      pvalue = request.getParameter(pname);
	      ccaRequest = ccaRequest + pname + "=" + URLEncoder.encode(pvalue,"UTF-8") + "&";
	 }
	 AesCryptUtil aesUtil=new AesCryptUtil(workingKey);
	 String encRequest = aesUtil.encrypt(ccaRequest);
	%>
 --%>	
	<!-- <form id="redirect" method="post" name="redirect" action="https://secure.ccavenue.com/transaction/transaction.do?command=initiateTransaction"/>  -->
	     <form id="nonseamless" method="post" name="redirect" action="https://test.ccavenue.com/transaction/transaction.do?command=initiateTransaction">
		<input type="hidden" id="encRequest" name="encRequest" value="${encRequest}"/>
		<input type="hidden" name="access_code" id="access_code" value="${access_code}"/>
		<script language='javascript'>document.redirect.submit();</script>
	</form>
	
 </body> 
</html>
