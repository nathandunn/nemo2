class UrlMappings {

	static mappings = {
        "205" (controller: "error", action: "internalError")
        "403" (controller: "error", action: "forbidden")
        "404" (controller: "error", action: "notFound")
        "500" (controller: "error", action: "internalError")
        "503" (controller: "error", action: "internalError")

		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

//		"/"(view:"/index")
        "/"{
            controller = "experiment"
        }
	}


}
