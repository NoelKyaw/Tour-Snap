using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using toursnapLib;

// NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service" in code, svc and config file together.
public class Service : IService
{
    const string dbConnection =
      "Data Source=.;Initial Catalog=TourSnap;Integrated Security=True";

    public string[] List()
    {
        return Photo.List();
    }

    public WCF_Photo GetPhoto(string imgPath)
    {
        Photo p = Photo.GetPhoto(imgPath);
        return new WCF_Photo(p.imgPath, p.description, p.location);
    }

    public int DeletePhoto(string imgPath)
    {
        return Photo.DeletePhoto(imgPath);
    }

    public int UpdatePhoto(WCF_Photo p)
    {
        return Photo.UpdatePhoto(
            new Photo(p.ImgPath, p.Description, p.Location));
    }

    public int InsertPhoto(WCF_Photo p)
    {
        return Photo.InsertPhoto(
            new Photo(p.ImgPath, p.Description, p.Location));
    }
}
