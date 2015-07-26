using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

public partial class upload : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        if (Request.HttpMethod.Equals("POST") && (file1.PostedFile != null) && (file1.PostedFile.ContentLength > 0))
        {
            string fn = System.IO.Path.GetFileName(file1.PostedFile.FileName);
            string SaveLocation = Server.MapPath("images") + "\\" + fn;
            try
            {
                file1.PostedFile.SaveAs(SaveLocation);
                Response.Write("The file has been uploaded.");
            }
            catch (Exception ex)
            {
                Response.Write("Error: " + ex.Message);
            }
        }
    }
}