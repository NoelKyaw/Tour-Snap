<%@ Page Language="C#" AutoEventWireup="true" CodeFile="upload.aspx.cs" Inherits="upload" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1" runat="server">
    <title></title>
</head>
<body>
   <form id="form1" enctype="multipart/form-data" runat="server">
    <div>
       <input type="file" id="file1" name="file1" runat="server" />
       <br/>
       <input type="submit" id="Submit1" value="Upload" runat="server" />
    </div>
    </form>
</body>
</html>

</html>
