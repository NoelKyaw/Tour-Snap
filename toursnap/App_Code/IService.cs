using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

// NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService" in both code and config file together.
[ServiceContract]
public interface IService
{

    [OperationContract]
    [WebGet(UriTemplate = "/List", ResponseFormat = WebMessageFormat.Json)]
    string[] List();

    [OperationContract]
    [WebGet(UriTemplate = "/GetPhoto/{imgPath}", ResponseFormat = WebMessageFormat.Json)]
    WCF_Photo GetPhoto(string imgPath);

    [OperationContract]
    [WebGet(UriTemplate = "/DeletePhoto/{imgPath}", ResponseFormat = WebMessageFormat.Json)]
    int DeletePhoto(string imgPath);

    [OperationContract]
    [WebInvoke(UriTemplate = "/UpdatePhoto", Method = "POST",
        RequestFormat = WebMessageFormat.Json,
        ResponseFormat = WebMessageFormat.Json)]
    int UpdatePhoto(WCF_Photo p);

    [OperationContract]
    [WebInvoke(UriTemplate = "InsertPhoto", Method = "POST",
        RequestFormat = WebMessageFormat.Json,
        ResponseFormat = WebMessageFormat.Json)]
    int InsertPhoto(WCF_Photo p);

	// TODO: Add your service operations here
}

// Use a data contract as illustrated in the sample below to add composite types to service operations.
[DataContract]
public class WCF_Photo
{
   
    [DataMember]
    public string ImgPath;

    [DataMember]
    public string Description;

    [DataMember]
    public string Location;

    public WCF_Photo(string ImgPath, string Description, string Location)
    {
        this.ImgPath = ImgPath;
        this.Description = Description;
        this.Location = Location;
    }
}
