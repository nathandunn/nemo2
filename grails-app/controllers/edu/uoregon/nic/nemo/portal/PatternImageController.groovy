package edu.uoregon.nic.nemo.portal

import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.springframework.dao.DataIntegrityViolationException

import javax.servlet.ServletContext

class PatternImageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    String emptyImageFilePath = new String("images/empty.png")
    byte[] emptyImage

    PatternImageController() {
        ServletContext servletContext = ServletContextHolder.servletContext
        String path = servletContext.getRealPath(emptyImageFilePath) ?: "./web-app/${emptyImageFilePath}"
        log.debug "current path: ${new File(".").absolutePath}"
        File emptyImageFile = new File(path)
        emptyImage = emptyImageFile.bytes
    }

    def viewImage(String id) {
        if (id) {
            PatternImage patternImage = PatternImage.findByPatternName(id.decodeURL())
            if (patternImage?.image) {
                response.outputStream << patternImage.image
                return
            }
        }

        response.outputStream << emptyImage
    }

    def deleteImage(String id){
        if (id) {
            PatternImage patternImage = PatternImage.findByPatternName(id.decodeURL())
            if (patternImage?.image) {
                patternImage.image = null
                if (patternImage.save(validate: true)){
                    flash.message = "Image removed ${patternImage.patternName}."
                }
                else{
                    flash.message = "Unable to remove image ${patternImage.patternName}."
                    log.error(patternImage.errors)
                }
            }
            flash.message = "No image to remove for ${patternImage.patternName}."
            redirect(controller: 'erpAnalysisResult', action: "edit", id: patternImage.erpAnalysisResult.id)
            return
        }
        flash.message = "Must pass key in [${id}]"
        redirect(controller: 'erpAnalysisResult', action: "list")
    }

    // newImage . . . id is instance key
    def upload(Integer id,String patternName) {
        ErpAnalysisResult erpAnalysisResult = ErpAnalysisResult.findById(id)
        def f = request.getFile('newImage')
        if (f.empty) {
            flash.message = 'File cannot be empty'
            redirect(controller: 'erpAnalysisResult', action: "edit", id: erpAnalysisResult.id)
            return
        }


        PatternImage patternImage = PatternImage.findByPatternName(patternName?.decodeURL())
        if (patternImage == null) {
            patternImage = new PatternImage()
            patternImage.erpAnalysisResult = erpAnalysisResult
            patternImage.patternName = patternName.decodeURL()
        }
        patternImage.image = f.bytes
        if(!patternImage.save(validate: true)){
            flash.message = 'There was a problem uploading the file.'
            log.error(patternImage.errors)
            redirect(controller: 'erpAnalysisResult', action: "edit", id: erpAnalysisResult.id)
        }
        else{
            flash.message = "Image uploaded for ${patternImage.patternName?.decodeURL()}"
            redirect(controller: 'erpAnalysisResult', action: "edit", id: erpAnalysisResult.id)
        }

    }


    def viewRawImage(String id) {
        if (id) {
            PatternImage patternImage = PatternImage.findByPatternName(id.decodeURL())
            if (patternImage?.rawImage) {
                response.outputStream << patternImage.rawImage
                return
            }
        }
        response.outputStream << emptyImage
    }

    def deleteRawImage(String id){
        if (id) {
            PatternImage patternImage = PatternImage.findByPatternName(id.decodeURL())
            if (patternImage?.rawImage) {
                patternImage.rawImage = null
                if (patternImage.save(validate: true)){
                    flash.message = "Raw image removed ${patternImage.patternName}."
                }
                else{
                    flash.message = "Unable to remove raw image ${patternImage.patternName}."
                    log.error(patternImage.errors)
                }
            }
            flash.message = "No raw image to remove for ${patternImage.patternName}."
            redirect(controller: 'erpAnalysisResult', action: "edit", id: patternImage.erpAnalysisResult.id)
            return
        }
        flash.message = "Must pass key in ${id}"
        redirect(controller: 'erpAnalysisResult', action: "list")
    }

    def uploadRaw(Integer id,String patternName) {
        ErpAnalysisResult erpAnalysisResult = ErpAnalysisResult.findById(id)
        def f = request.getFile('newImage')
        if (f.empty) {
            flash.message = 'File cannot be empty'
            redirect(controller: 'erpAnalysisResult', action: "edit", id: erpAnalysisResult.id)
            return
        }

        PatternImage patternImage = PatternImage.findByPatternName(patternName?.decodeURL())
        if (patternImage == null) {
            patternImage = new PatternImage()
            patternImage.erpAnalysisResult = erpAnalysisResult
            patternImage.patternName = patternName.decodeURL()
        }
        patternImage.rawImage = f.bytes
        if(!patternImage.save(validate: true)){
            flash.message = 'There was a problem uploading the file.'
            log.error(patternImage.errors)
            redirect(controller: 'erpAnalysisResult', action: "edit", id: erpAnalysisResult.id)
            return
        }
        else{
            flash.message = "Raw Image uploaded for ${patternImage.patternName?.decodeURL()}"
            redirect(controller: 'erpAnalysisResult', action: "edit", id: erpAnalysisResult.id)
        }
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [patternImageInstanceList: PatternImage.list(params), patternImageInstanceTotal: PatternImage.count()]
    }

    def create() {
        [patternImageInstance: new PatternImage(params)]
    }

    def save() {
        def patternImageInstance = new PatternImage(params)
        if (!patternImageInstance.save(flush: true)) {
            render(view: "create", model: [patternImageInstance: patternImageInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'patternImage.label', default: 'PatternImage'), patternImageInstance.patternName])
        redirect(action: "show", id: patternImageInstance.id)
    }

    def show(Integer id) {
        def patternImageInstance = PatternImage.get(id)
        if (!patternImageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patternImage.label', default: 'PatternImage'), id])
            redirect(action: "list")
            return
        }

        [patternImageInstance: patternImageInstance]
    }

    def edit(Integer id) {
        def patternImageInstance = PatternImage.get(id)
        if (!patternImageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patternImage.label', default: 'PatternImage'), id])
            redirect(action: "list")
            return
        }

        [patternImageInstance: patternImageInstance]
    }

    def update(Integer id) {
        def patternImageInstance = PatternImage.get(id)
        if (!patternImageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patternImage.label', default: 'PatternImage'), id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (patternImageInstance.version > version) {
                patternImageInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'patternImage.label', default: 'PatternImage')] as Object[],
                        "Another user has updated this PatternImage while you were editing")
                render(view: "edit", model: [patternImageInstance: patternImageInstance])
                return
            }
        }

        patternImageInstance.properties = params

        if (!patternImageInstance.save(flush: true)) {
            println "failed to save "
            render(view: "edit", model: [patternImageInstance: patternImageInstance])
            return
        }
        println "successfully saved "

        flash.message = message(code: 'default.updated.message', args: [message(code: 'patternImage.label', default: 'PatternImage'), patternImageInstance.patternName])
        redirect(action: "show", id: patternImageInstance.id)
    }

    def delete(Integer id) {
        def patternImageInstance = PatternImage.get(id)
        if (!patternImageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patternImage.label', default: 'PatternImage'), id])
            redirect(action: "list")
            return
        }

        try {
            patternImageInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'patternImage.label', default: 'PatternImage'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'patternImage.label', default: 'PatternImage'), id])
            redirect(action: "show", id: id)
        }
    }
}
