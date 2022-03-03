/*
 * Crafting Dead
 * Copyright (C) 2022  NexusNode LTD
 *
 * This Non-Commercial Software License Agreement (the "Agreement") is made between you (the "Licensee") and NEXUSNODE (BRAD HUNTER). (the "Licensor").
 * By installing or otherwise using Crafting Dead (the "Software"), you agree to be bound by the terms and conditions of this Agreement as may be revised from time to time at Licensor's sole discretion.
 *
 * If you do not agree to the terms and conditions of this Agreement do not download, copy, reproduce or otherwise use any of the source code available online at any time.
 *
 * https://github.com/nexusnode/crafting-dead/blob/1.18.x/LICENSE.txt
 *
 * https://craftingdead.net/terms.php
 */

package com.craftingdead.immerse.client.util;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class LoggingErrorHandler implements ErrorHandler {

  public static final LoggingErrorHandler INSTANCE = new LoggingErrorHandler();

  private static final Logger logger = LogUtils.getLogger();

  private LoggingErrorHandler() {}

  @Override
  public void warning(SAXParseException exception) throws SAXException {
    logger.warn("[SAX Parser Warning] ", exception);
  }

  @Override
  public void error(SAXParseException exception) throws SAXException {
    logger.error("[SAX Parser Error] ", exception);
  }

  @Override
  public void fatalError(SAXParseException exception) throws SAXException {
    logger.error("[SAX Parser Fatal Error] ", exception);
  }
}
