<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" omit-xml-declaration="yes" indent="yes"/>
    <xsl:param name="projectName"/>

    <xsl:template match="/">
        <html>
            <head>
                <title>
                    <xsl:value-of select="$projectName"/> groups
                </title>
            </head>
            <body>
                <h1>
                    <xsl:value-of select="$projectName"/> groups
                </h1>
                <table border="1" cellpadding="8" cellspacing="0">
                    <tr>
                        <th>Group</th>
                        <th>Type</th>
                    </tr>
                    <xsl:for-each
                            select="/*[name()='Payload']/*[name()='Projects']/*[name()='Project'][@name=$projectName]/*[name()='Group']">
                        <tr>
                            <td>
                                <xsl:value-of select="@name"/>
                            </td>
                            <td>
                                <xsl:value-of select="@type"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>