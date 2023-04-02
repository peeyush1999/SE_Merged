function saveUserCookie()
{
    var username = document.getElementById("inputUsername").value;
    var password = document.getElementById("inputPasswordConfirm").value;
    // console.log("username: " + username);
    // console.log("password: " + password);
    // console.log(getCookie(username))
    if(getCookie(username) == "")
        setCookie(username, password, 10);
}

function addSong()
{
    // console.log("AddSongHit");
    var fileName = document.getElementById("fileName").innerText;
    var username = document.getElementById("username").innerText;
    var visibility = document.getElementById("visibility").value;
    var albumArtist = document.getElementById("albumArtist").value;
    var album = document.getElementById("album").value;
    var artist = document.getElementById("artist").value;
    var title = document.getElementById("title").value;

    let object = {
        fileName:fileName,
        username:username,
        visibility:visibility,
        AlbumArtist:albumArtist,
        Album:album,
        Artist:artist,
        Title:title
    }
    var songs=[];

    if(getCookieObject("song")!="")
    {
        songs = getCookieObject("song");
    }
    songs.push(object);

    setCookieObject("song",songs,10);
    // console.log("AddSongFinished");
}
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}


function addPlayList()
{
    // console.log("AddPlayListHit");
    var username = document.getElementById("username").innerText;
    var visibility = document.getElementById("inputPlaylistVisibility").value;
    var playListName = document.getElementById("inputPlaylistName").value;
    var coordinator = [];
    var songs=[];
    let object = {
        username:username,
        visibility:visibility,
        playListName:playListName,
        coordinator:coordinator,
        songs:songs
    }
    var playList =[];

    if(getCookieObject("playList")!="")
    {
        playList = getCookieObject("playList");
    }
    playList.push(object);

    setCookieObject("playList",playList,10);
    // console.log("AddPlayListFinished");
}


function filterAlbum()
{
    
    // console.log("FilterAlbumisHit");
    var username = document.getElementById("username").innerText;
    var songLst = getCookieObject("song");
    var hmap = {}
    for(i=0;i<songLst.length;i++)
    {
        if(songLst[i]["username"] == username || songLst[i]["visibility"] == "Public" )
            hmap[songLst[i]["AlbumArtist"]]=2;
    }
    
    var ele = document.getElementsByClassName("albumName");
    
    for(i=0;i<ele.length;i++)
    {
        var album = ele[i].innerHTML;
        if(hmap[album]!=2)
        {
            console.log(album);
            ele[i].parentElement.parentElement.parentElement.parentElement.parentElement.remove();
            // // ele[i].parentElement.parentElement.parentElement.parentElement.parentElement.style.display = "none";
            i--;
        }
    }
     
}
// write a similar function to filter album for artist
function filterArtist()
{
    
    // console.log("FilterArtistisHit");
    var username = document.getElementById("username").innerText;
    var songLst = getCookieObject("song");
    var hmap = {}
    for(i=0;i<songLst.length;i++)
    {
        if(songLst[i]["username"] == username || songLst[i]["visibility"] == "Public" )
            hmap[songLst[i]["Artist"]]=2;
    }
    
    var ele = document.getElementsByClassName("artistcard");
    for(i=0;i<ele.length;i++)
    {
        var artist = ele[i].innerHTML;
        if(hmap[artist]!=2)
        {
            // console.log(artist);
            ele[i].parentElement.parentElement.parentElement.parentElement.parentElement.remove();
            i--;
        }
    }
     
}

function filterSong()
{
    // console.log("FilterSongsHit");
    var username = document.getElementById("username").innerText;
    var songLst = getCookieObject("song");
    var hmap = {}
    for(i=0;i<songLst.length;i++)
    {
        if(songLst[i]["username"] == username || songLst[i]["visibility"] == "Public" )
            hmap[songLst[i]["Title"]]=2;
    }

    var ele = document.getElementsByClassName("songTitle");
    for(i=0;i<ele.length;i++)
    {
        var song = ele[i].innerHTML;
        if(hmap[song]!=2)
        {
            console.log(song);
            // ele[i].parentElement.parentElement.parentElement.parentElement.parentElement.remove();
            ele[i].parentElement.parentElement.remove();
            i--;
        }
    }

}

function filterPlayList()
{
    // console.log("FilterPlayListHit");
    var username = document.getElementById("username").innerText;
    var playList = getCookieObject("playList");
    var hmap = {}
    for(i=0;i<playList.length;i++)
    {
        if(playList[i]["username"] == username || playList[i]["visibility"] == "Public" )
            hmap[playList[i]["playListName"]]=2;
    }

    var ele = document.getElementsByClassName("userPlayList");
    for(i=0;i<ele.length;i++)
    {
        var song = ele[i].innerHTML;
        if(hmap[song]!=2)
        {
            console.log(song);
            // ele[i].parentElement.parentElement.parentElement.parentElement.parentElement.remove();
            ele[i].remove();
            i--;
        }
    }

}

function dropFilter()
{
    // var lst = document.getElementsByClassName("newPlayList")[0].firstElementChild.innerHTML
    var username = document.getElementById("username").innerText;
    var lst = document.getElementsByClassName("newPlayList");
    var playList = getCookieObject("playList");
    var hmap={};

    for(i=0;i<playList.length;i++)
    {
        if(playList[i]["username"] == username)
        {    
            hmap[playList[i]["playListName"]]=2;
        }
        else if(playList[i]["visibility"] == "Public")
        {
            var len = playList[i]["coordinator"].length;
                for(var j=0;j<len;j++)
                {
                    if(playList[i]["coordinator"][j] == username)
                    {
                        hmap[playList[i]["playListName"]]=2;
                    }
                }
        }
    }

    for(i=0;i<lst.length;i++)
    {
        if(hmap[lst[i].firstElementChild.innerHTML]!=2)
        {
            lst[i].remove();
            i--;
        }
    }
}

function filterplaylistMode()
{
    // console.log("FilterPlayListHit");
    
    var username = document.getElementById("username").innerText;
    var playList = getCookieObject("playList");
    var flag=0;
    var name = document.getElementsByClassName("playListName")[0].innerHTML;
    for(i=0;i<playList.length;i++)
    {
        if(playList[i]["username"] == username && playList[i]["playListName"] == name)
            flag=1;
        else if(playList[i]["playListName"] == name && playList[i]["visibility"] == "Public" )
            {
                var len = playList[i]["coordinator"].length;
                for(var j=0;j<len;j++)
                {
                    if(playList[i]["coordinator"][j] == username)
                    {
                        flag=2;
                    }
                }
            }
    }

    if(flag==0)
    {
        document.getElementsByClassName("editButton")[0].style.display = "none";
        document.getElementsByClassName("removePlaylistClass")[0].style.display = "none";

        var len = document.getElementsByClassName("remove").length;
        for(var i=0;i<len;i++)
        {
            document.getElementsByClassName("remove")[i].style.display = "none";
        }
    }
    if(flag==2)
    {
        document.getElementsByClassName("editButton")[0].style.display = "none";
        document.getElementsByClassName("removePlaylistClass")[0].style.display = "none";
        var len = document.getElementsByClassName("remove").length;
        for(var i=0;i<len;i++)
        {
            document.getElementsByClassName("remove")[i].style.display = "none";
        }
    }
}

function addCoordinator(form)
{
    // console.log("AddCoordinatorHit");
    var username = document.getElementById("username").innerText;
    var playList = getCookieObject("playList");
    var name = form.playList.value;
    var coordinator = form.coordinator.value;
    var visibility = form.visibility.value;

   

    // console.log("name: "+name);
    // console.log("coordinator: "+coordinator);
    // console.log("visibility: "+visibility);

    for(i=0;i<playList.length;i++)
    {
        if(playList[i]["playListName"] == name)
        {
            if(coordinator!=username && coordinator!="")
                playList[i]["coordinator"].push(coordinator);
            
            if(visibility!="")
                playList[i]["visibility"] = visibility;
        }
    }
    setCookieObject("playList",playList,10);
    // console.log("AddCoordinatorFinished");
}

function removeCoordinator(form)
{
    // console.log("RemoveCoordinatorHit");
    var username = document.getElementById("username").innerText;
    var playList = getCookieObject("playList");
    var name = form.playList.value;
    var coordinator = form.coordinator.value;
    var visibility = form.visibility.value;

    if(coordinator=="")
        return;
    // console.log("name: "+name);
    // console.log("coordinator: "+coordinator);
    // console.log("visibility: "+visibility);

    for(i=0;i<playList.length;i++)
    {
        if(playList[i]["playListName"] == name)
        {
            var len = playList[i]["coordinator"].length;

            if(coordinator!=username && coordinator!="")
            {
                for(var j=0;j<len;j++)
                {
                    if(playList[i]["coordinator"][j] == coordinator)
                    {
                        playList[i]["coordinator"].splice(j,1);
                    }
                }
            }
        }
    }
    setCookieObject("playList",playList,10);
    // console.log("RemoveCoordinatorFinished");
}