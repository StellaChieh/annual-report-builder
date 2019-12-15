package cwb.cmt.surface.pdfProcess;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.svg.SVGDocument;

import com.itextpdf.awt.PdfGraphics2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class SvgToPdf {

    /** The SVG document factory. */
    private SAXSVGDocumentFactory factory;
    
    /** Creates an SvgToPdf object. */
    public SvgToPdf() {
    	String parser = XMLResourceDescriptor.getXMLParserClassName();
        factory = new SAXSVGDocumentFactory(parser);
    }
    
    /**
     * Draws an SVG file to a PdfTemplate.
     * @param template the template to which the SVG has to be drawn.
     * @param resource the SVG content.
     * @throws IOException
     */
    private void drawSvg(PdfTemplate template, String resource) throws IOException {
        UserAgent userAgent = new UserAgentAdapter();
        DocumentLoader loader = new DocumentLoader(userAgent);
        
        // The SVG bridge context
        BridgeContext ctx = new BridgeContext(userAgent, loader);
        ctx.setDynamicState(BridgeContext.DYNAMIC);
        
        // The GVT builder
        GVTBuilder builder = new GVTBuilder();
    	Graphics2D g2d = new PdfGraphics2D(template, 595, 842);
        SVGDocument svgDocument = factory.createSVGDocument(new File(resource).toURI().toURL().toString());
        GraphicsNode graphics = builder.build(ctx, svgDocument);
        graphics.paint(g2d);
        g2d.dispose();
    }
    
    /**
     * Creates a PDF document.
     * @param srcFilename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException
     * @throws SQLException 
     */
    public void createPdf(String srcFilename) throws IOException, DocumentException {
    	Document document = new Document(new Rectangle(595, 842));
        String resultFilename = Paths.get(FilenameUtils.removeExtension(srcFilename)).toString() + ".pdf"; 
    	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(resultFilename));
    	document.open();
    	
        PdfContentByte cb = writer.getDirectContent();
        PdfTemplate template = cb.createTemplate(595, 842);
        drawSvg(template, srcFilename);
        cb.addTemplate(template, 0, 0);
        document.close();
    }
    
    /**
     * Main method.
     * @param  args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
//        new SvgToPdf().createPdf(RESULT);
    }
}
