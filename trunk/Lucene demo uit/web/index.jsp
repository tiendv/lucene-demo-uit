<%-- 
    Document   : index
    Created on : Sep 26, 2012, 1:38:42 PM
    Author     : Dungit86
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link type="text/css" href="css/smoothness/jquery-ui-1.8.23.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.23.custom.min.js"></script>

        <title>AutoComplete</title>
    </head>
    <style>
        #project-label {
            display: block;
            font-weight: bold;
            margin-bottom: 1em;
        }
        #project-icon {
            float: left;
            height: 32px;
            width: 32px;
        }
        #project-description {
            margin: 0;
            padding: 0;
        }
    </style>
    <script type="text/javascript">
        $(function(){
            //Autocomplete
            $("#autocomplete").autocomplete({
                delay: 0,
                source: function(request, response) {
                    $.ajax({
                        url: 'result?text='+ encodeURIComponent(request.term),
                        dataType: 'json',
                        success: function(json) {
                            response($.map(json.suggestions, function(item) {
                                return {
                                    label: item.name,
                                    value: item.name,
                                    icon: item.icon
                                }
                            }));
                        }
                    });
                }
            })
            .data( "autocomplete" )._renderItem = function( ul, item ) {
                return $( "<li>" )
                .data( "item.autocomplete", item )
                .append( "<a><img src=\"images/" +item.icon+ "\" height=\"15\" width=\"15\"> "+ item.label +"</a>" )
                .appendTo( ul );
            };
        });
        /*
        var projects = [
            {
                value: "jquery",
                label: "jQuery",
                desc: "the write less, do more, JavaScript library",
                icon: "jquery_32x32.png"
            },
            {
                value: "jquery-ui",
                label: "jQuery UI",
                desc: "the official user interface library for jQuery",
                icon: "jqueryui_32x32.png"
            },
            {
                value: "sizzlejs",
                label: "Sizzle JS",
                desc: "a pure-JavaScript CSS selector engine",
                icon: "sizzlejs_32x32.png"
            }
        ];
 
        $( "#project" ).autocomplete({
            minLength: 0,
            source: projects,
            focus: function( event, ui ) {
                $( "#project" ).val( ui.item.label );
                return false;
            },
            select: function( event, ui ) {
                $( "#project" ).val( ui.item.label );
                $( "#project-id" ).val( ui.item.value );
                $( "#project-description" ).html( ui.item.desc );
                $( "#project-icon" ).attr( "src", "images/" + ui.item.icon );
 
                return false;
            }
        })
        .data( "autocomplete" )._renderItem = function( ul, item ) {
            return $( "<li>" )
            .data( "item.autocomplete", item )
            .append( "<a>" + item.label + "<br>" + item.desc + "</a>" )
            .appendTo( ul );
        };*/
    </script>

    <body>
        <div>
            <input id="autocomplete" style="z-index: 100; position: relative" title="type &quot;a&quot;" />
                                      <img src="images/keyword.jpg" height="15" width="15">
            </div>
        </body>
    </html>
