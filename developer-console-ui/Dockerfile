FROM node:16.14-alpine AS app
RUN addgroup -g 1001 -S nodejs
RUN adduser -u 1001 -S nextjs
WORKDIR /app
COPY --chown=nextjs:nodejs developer-console-ui/app/next.config.js ./next.config.js
COPY --chown=nextjs:nodejs developer-console-ui/app/public ./public
COPY --chown=nextjs:nodejs developer-console-ui/app/.next ./.next
COPY --chown=nextjs:nodejs developer-console-ui/app/node_modules ./node_modules
COPY --chown=nextjs:nodejs developer-console-ui/app/package.json ./package.json
USER nextjs
CMD ["yarn", "start"]