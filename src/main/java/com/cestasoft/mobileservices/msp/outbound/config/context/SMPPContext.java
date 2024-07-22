package com.cestasoft.mobileservices.msp.outbound.config.context;

import com.cestasoft.mobileservices.framework.data.DataStore;
import com.cestasoft.mobileservices.msp.outbound.component.amqp.MessageConsumer;
import com.cestasoft.mobileservices.msp.outbound.util.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Provides configuration context for SMPP client transactions
 * @author ezra.k@cestasoft.com
 */
public class SMPPContext {

    final Logger logger = LoggerFactory.getLogger(SMPPContext.class);

    private Document _context;

    private final DataStore _ds;

    public SMPPContext(DataStore ds) {
        this._ds = ds;
    }

    public Document context(String reference) {
        if (_context.containsKey(reference))
            return (Document) _context.get(reference);
        return null;
    }

    public void load() {
        try {
            Bson filter = new Document().append("status", "active");
            List<Document> channels = _ds.query(filter, Constants.DATASTORE_REF_CHANNEL_CONFIG);
            this._context = new Document().append("channels", channels);
        } catch (Exception x) {
            logger.error("could not load config, reverting to context", x);
        }
    }

    public Document context() {
        return this._context;
    }
}
