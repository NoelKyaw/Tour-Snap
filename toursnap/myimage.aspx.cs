using System;
using System.Drawing;
using System.Drawing.Imaging;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.IO;

public partial class myimage : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        int size = 100;
        string imagePath = String.Format("{0}/{1}", Server.MapPath("images"), Request.QueryString["image"]);
        string n = Request.QueryString["size"];

        if (n != null && n.Length > 0)
            size = Int32.Parse(n);
        if (File.Exists(imagePath))
        {
            //--------------- Dynamically changing image size --------------------------  
            Response.ClearContent();
            Response.ContentType = getContentType(imagePath);
            byte[] buffer = getResizedImage(imagePath, size, size);
            Response.BinaryWrite(buffer);
            Response.AddHeader("Content-Length", buffer.Length.ToString());

            Response.End();
        }
    }

    byte[] getResizedImage(String path, int width, int height)
    {
        Bitmap imgIn = new Bitmap(path);
        double y = imgIn.Height;
        double x = imgIn.Width;

        double factor = 1;
        if (width > 0)
        {
            factor = width / x;
        }
        else if (height > 0)
        {
            factor = height / y;
        }
        System.IO.MemoryStream outStream = new System.IO.MemoryStream();
        Bitmap imgOut = new Bitmap((int)(x * factor), (int)(y * factor));

        // Set DPI of image (xDpi, yDpi)
        imgOut.SetResolution(72, 72);

        Graphics g = Graphics.FromImage(imgOut);
        g.Clear(Color.White);
        g.DrawImage(imgIn, new Rectangle(0, 0, (int)(factor * x), (int)(factor * y)),
          new Rectangle(0, 0, (int)x, (int)y), GraphicsUnit.Pixel);

        imgOut.Save(outStream, getImageFormat(path));
        return outStream.ToArray();
    }

    string getContentType(String path)
    {
        switch (Path.GetExtension(path))
        {
            case ".bmp": return "image/bmp";
            case ".gif": return "image/gif";
            case ".jpg": return "image/jpg";
            case ".png": return "image/png";
            default: break;
        }
        return "";
    }

    ImageFormat getImageFormat(String path)
    {
        switch (Path.GetExtension(path))
        {
            case ".bmp": return ImageFormat.Bmp;
            case ".gif": return ImageFormat.Gif;
            case ".jpg": return ImageFormat.Jpeg;
            case ".png": return ImageFormat.Png;
            default: break;
        }
        return ImageFormat.Jpeg;
    }
}